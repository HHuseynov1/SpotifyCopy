package com.example.spotifycopy.data.remote

import com.example.spotifycopy.data.other.Constants.SONG_COLLECTION
import com.example.spotifycopy.data.entities.Song
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class MusicDatabase {
    private val fireStore = FirebaseFirestore.getInstance()
    private val songCollection = fireStore.collection(SONG_COLLECTION)

    suspend fun getAllSongs():List<Song>{
        return try{
            songCollection.get().await().toObjects(Song::class.java)
        }catch (e:Exception){
            emptyList()
        }
    }
}