package uk.ac.tees.mad.univid

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val auth : FirebaseAuth,
    val firestore : FirebaseFirestore,
    val storage : FirebaseStorage
) : ViewModel() {

    val isLoading = mutableStateOf(false)
    val isSignedIn = mutableStateOf(false)

    init {
        if(auth.currentUser != null){
            isSignedIn.value = true
        }
    }

    fun signUp(context : Context, name: String, email: String, password: String, phone: String){
        isLoading.value = true
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            firestore.collection("users").document(it.user!!.uid).set(hashMapOf(
                "name" to name,
                "email" to email,
                "password" to password,
                "phone" to phone) )
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
            isLoading.value = false
            isSignedIn.value = true
        }.addOnFailureListener {
            isLoading.value = false
            Toast.makeText(context,"${it.message}", Toast.LENGTH_LONG).show()
        }
    }

}