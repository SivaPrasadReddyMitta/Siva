package uk.ac.tees.mad.univid

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.internal.Contexts.getApplication
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

    init {
        if(auth.currentUser != null){
            isSignedIn.value = true
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

    fun addItem(context : Context,title: String, description: String, image: Uri, location : String){
        isLoading.value = true
        val storageRef = storage.reference
        val itemsRef = storageRef.child("items/${image.lastPathSegment}")
        val uploadTask = itemsRef.putFile(image)
        uploadTask.addOnSuccessListener { item ->
                val downloadUrl = item.storage.downloadUrl
            val itemData = ItemData(
                name = userData.value!!.name,
                number = userData.value!!.phonenumber,
                title = title,
                description = description,
                image = downloadUrl.toString(),
                location = location
            )
            Log.d("Item Data", itemData.toString())
                firestore.collection(ITEMS).add(itemData).addOnSuccessListener { item->
                    val id = item.id
                    val itemRef = itemData.copy(id = id)
                    firestore.collection(ITEMS).document(id).set(itemRef)
                    isLoading.value = false
                    Log.d("Item Post", itemRef.toString())
                    Toast.makeText(context, "Item added successfully", Toast.LENGTH_LONG).show()
                }.addOnFailureListener {
                    isLoading.value = false
                    Toast.makeText(context, "${it.message}", Toast.LENGTH_LONG).show()
                }

        }.addOnFailureListener{
            Log.d("Item Post", it.stackTrace.toString())
            isLoading.value = false
            Toast.makeText(context, "${it.message}", Toast.LENGTH_LONG).show()
        }

    }

}