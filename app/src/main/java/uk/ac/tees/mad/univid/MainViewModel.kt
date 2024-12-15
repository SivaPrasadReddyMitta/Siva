package uk.ac.tees.mad.univid

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.ac.tees.mad.univid.Utils.ITEMS
import uk.ac.tees.mad.univid.Utils.USERS
import uk.ac.tees.mad.univid.models.remote.ItemData
import uk.ac.tees.mad.univid.models.remote.UserData
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val auth : FirebaseAuth,
    val firestore : FirebaseFirestore,
    val storage : FirebaseStorage
) : ViewModel() {

    val isLoading = mutableStateOf(false)
    val isSignedIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val item = mutableStateOf<List<ItemData>>(emptyList<ItemData>())

    init {
        if(auth.currentUser != null){
            isSignedIn.value = true
        }
        getAllFromFirebase()
    }

    fun getAllFromFirebase(){
        firestore.collection(ITEMS).get().addOnSuccessListener {
            item.value = it.toObjects(ItemData::class.java)
            Log.d("Item", "${item.value}")
        }.addOnFailureListener {
            Log.d("Item", "${it.message}")
        }
    }
    fun signUp(context: Context, name: String, email: String, password: String, phone: String) {
        isLoading.value = true
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            val uid = it.user!!.uid // Retrieve the user's UID
            val user = UserData(
                uid = uid,
                name = name,
                email = email,
                password = password,
                phonenumber = phone
            )
            firestore.collection(USERS).document(uid).set(user).addOnSuccessListener {
                getUserData(context, uid)
                isSignedIn.value = true
                isLoading.value = false
            }.addOnFailureListener { e ->
                isLoading.value = false
                Toast.makeText(context, "Failed to save user data: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            isLoading.value = false
            Toast.makeText(context, "${it.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun signIn(context: Context, email : String, password: String){
        isLoading.value = true
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            getUserData(context,it.user!!.uid)
            isLoading.value = false
            isSignedIn.value = true
        }.addOnFailureListener {
            isLoading.value = false
            Toast.makeText(context,"${it.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun getUserData(context : Context , uid: String){
        firestore.collection(USERS).document(uid).get().addOnSuccessListener {
            userData.value = it.toObject(UserData::class.java)
            Log.d("USER", "${userData.value}")
        }.addOnFailureListener {
            Toast.makeText(context, "${it.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun addItem(context: Context, title: String, description: String, image: Uri, location: String) {
        getUserData(context, auth.currentUser!!.uid)
        isLoading.value = true
        val storageRef = storage.reference
        val itemsRef = storageRef.child("items/${image.lastPathSegment}")
        val uploadTask = itemsRef.putFile(image)

        uploadTask.addOnSuccessListener { item ->
            // Retrieve the download URL once the file has been uploaded
            itemsRef.downloadUrl.addOnSuccessListener { uri ->
                val downloadUrl = uri.toString()
                Log.d("Item Upload", "Download URL: $downloadUrl")

                // Create the itemData object with the download URL
                val itemData = ItemData(
                    name = userData.value!!.name,
                    number = userData.value!!.phonenumber,
                    title = title,
                    description = description,
                    image = downloadUrl,
                    location = location
                )
                Log.d("Item Data", itemData.toString())

                // Add the item to Firestore
                firestore.collection(ITEMS).add(itemData).addOnSuccessListener { item ->
                    val id = item.id
                    val itemRef = itemData.copy(id = id)
                    firestore.collection(ITEMS).document(id).set(itemRef)
                    isLoading.value = false
                    Log.d("Item Post", itemRef.toString())
                    Toast.makeText(context, "Item added successfully", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    isLoading.value = false
                    Toast.makeText(context, "Failed to add item: ${it.message}", Toast.LENGTH_LONG).show()
                }

            }.addOnFailureListener {
                isLoading.value = false
                Log.e("Item Upload", "Failed to get download URL: ${it.message}")
                Toast.makeText(context, "Failed to get download URL: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }.addOnFailureListener {
            isLoading.value = false
            Log.e("Item Upload", "File upload failed: ${it.message}")
            Toast.makeText(context, "File upload failed: ${it.message}", Toast.LENGTH_LONG).show()
        }
    }

}