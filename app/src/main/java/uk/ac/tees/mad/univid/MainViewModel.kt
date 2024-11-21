package uk.ac.tees.mad.univid

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.ac.tees.mad.univid.Utils.USERS
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

    fun signUp(context : Context, name: String, email: String, password: String, phone: String){
        isLoading.value = true
        val user = UserData(
            name = name,
            email = email,
            password = password,
            phonenumber = phone
        )
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            firestore.collection(USERS).document(it.user!!.uid).set(user)
            getUserData(context, it.user!!.uid)
            isSignedIn.value = true
            isLoading.value = false
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

}