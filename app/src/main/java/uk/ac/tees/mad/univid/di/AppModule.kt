package uk.ac.tees.mad.univid.di

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun providesAuth(): FirebaseAuth = Firebase.auth

    @Provides
    fun providesFirestore() : FirebaseFirestore = Firebase.firestore

    @Provides
    fun providesStorage() : FirebaseStorage = Firebase.storage
}