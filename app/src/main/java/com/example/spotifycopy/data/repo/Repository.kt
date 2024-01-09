package com.example.spotifycopy.data.repo

import com.example.spotifycopy.data.entities.Artists
import com.example.spotifycopy.data.entities.Categories
import com.example.spotifycopy.data.entities.Song
import com.example.spotifycopy.data.entities.User
import com.example.spotifycopy.data.other.Constants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class Repository {
    private val fireStore = FirebaseFirestore.getInstance()
    private val artistsCollection = fireStore.collection(Constants.ARTIST_COLLECTION)

    suspend fun getAllArtists():List<Artists>{
        return try{
            artistsCollection.get().await().toObjects(Artists::class.java)
        }catch (e:Exception){
            emptyList()
        }
    }

    private val userCollection = fireStore.collection(Constants.USER_COLLECTION)

    suspend fun getUser():List<User>{
        return try{
            userCollection.get().await().toObjects(User::class.java)
        }catch (e:Exception){
            emptyList()
        }
    }

    private val categoryCollection = fireStore.collection(Constants.CATEGORY_COLLECTION)

    suspend fun getAllCategories(): List<Categories> {
        return try {
            categoryCollection.get().await().toObjects(Categories::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private val songCollection = fireStore.collection(Constants.SONG_COLLECTION)

    suspend fun getAllSongs():List<Song>{
        return try{
            songCollection.get().await().toObjects(Song::class.java)
        }catch (e:Exception){
            emptyList()
        }
    }
}