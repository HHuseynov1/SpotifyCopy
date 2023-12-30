package com.example.spotifycopy.data.remote

import com.example.spotifycopy.data.entities.Song
import com.example.spotifycopy.data.entities.User
import com.example.spotifycopy.data.other.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserDatabase {
    private val fireStore = FirebaseFirestore.getInstance()
    private val songCollection = fireStore.collection(Constants.USER_COLLECTION)

    suspend fun getUser():List<User>{
        return try{
            songCollection.get().await().toObjects(User::class.java)
        }catch (e:Exception){
            emptyList()
        }
    }
}