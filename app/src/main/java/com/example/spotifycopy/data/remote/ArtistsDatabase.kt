package com.example.spotifycopy.data.remote

import com.example.spotifycopy.data.entities.Artists
import com.example.spotifycopy.data.other.Constants
import com.example.spotifycopy.presentation.models.ArtistsModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ArtistsDatabase {
    private val fireStore = FirebaseFirestore.getInstance()
    private val artistsCollection = fireStore.collection(Constants.ARTIST_COLLECTION)

    suspend fun getAllArtists():List<Artists>{
        return try{
            artistsCollection.get().await().toObjects(Artists::class.java)
        }catch (e:Exception){
            emptyList()
        }
    }
}