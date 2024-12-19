package uk.ac.tees.mad.univid.di

import android.content.Context
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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.univid.data.ItemDao
import uk.ac.tees.mad.univid.data.ItemDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ItemDatabase {
        return ItemDatabase.getDatabase(context)
    }

    @Provides
    fun provideUserDao(database: ItemDatabase): ItemDao {
        return database.itemDao()
    }


    @Provides
    fun providesAuth(): FirebaseAuth = Firebase.auth

    @Provides
    fun providesFirestore() : FirebaseFirestore = Firebase.firestore

    @Provides
    fun providesStorage() : FirebaseStorage = Firebase.storage
}