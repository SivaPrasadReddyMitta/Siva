package uk.ac.tees.mad.univid

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.univid.Utils.ITEMS
import uk.ac.tees.mad.univid.Utils.USERS
import uk.ac.tees.mad.univid.models.local.Items
import uk.ac.tees.mad.univid.models.remote.ItemData
import uk.ac.tees.mad.univid.models.remote.UserData
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val auth : FirebaseAuth,
    val firestore : FirebaseFirestore,
    val storage : FirebaseStorage,
    private val appRepository: AppRepository
) : ViewModel() {

    val isLoading = mutableStateOf(false)
    val isSignedIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)

    private val _item = MutableStateFlow<List<Items>>(emptyList())
    val item: StateFlow<List<Items>> get() = _item

    private val _items = MutableStateFlow<List<Items>>(emptyList())


    init {
        if(auth.currentUser != null){
            isSignedIn.value = true
            getUserDataWothoutCOntext(auth.currentUser!!.uid)
        }
        getAllFromFirebase()
    }

    fun getAllFromFirebase(){
        firestore.collection(ITEMS).get().addOnSuccessListener {
            val itemDataList = it.toObjects(ItemData::class.java)
            _items.value = itemDataList.map { it.toItems() }
            insertInDatabase(_items.value)
            Log.d("Item", "${_items.value}")
        }.addOnFailureListener {
            Log.d("Item", "${it.message}")
        }
    }

    fun insertInDatabase(item : List<Items>){
        viewModelScope.launch {
            appRepository.delete()
            appRepository.addData(item)
            appRepository.getAllFromDB().collect{itemList->
                _item.value = itemList
            }
        }
    }

    fun ItemData.toItems(): Items {
        return Items(
            id = id,
            name = name,
            number = number,
            title = title,
            description = description,
            image = image,
            location = location
        )
    }

    fun updateUserData(name: String, email: String, phone: String){
        isLoading.value = true
        val user = userData.value?.copy(
            name = name,
            email = email,
            phonenumber = phone
        )
        Log.d("Updated User", user.toString())
        firestore.collection(USERS).document(auth.currentUser!!.uid).set(user!!).addOnSuccessListener {
            getUserDataWothoutCOntext(auth.currentUser!!.uid)
            isLoading.value = false
        }.addOnFailureListener {
            Log.d("Update Failed", "${it.message}")
            isLoading.value = false
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

    fun getUserDataWothoutCOntext(uid: String){
        firestore.collection(USERS).document(uid).get().addOnSuccessListener {
            userData.value = it.toObject(UserData::class.java)
            Log.d("USER", "${userData.value}")
        }.addOnFailureListener {
            Log.d( "Failed to load user","${it.message}")
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