package com.example.spotifycopy.view.ui.startListeningFragmentArtists

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.entities.Artists
import com.example.spotifycopy.data.remote.ArtistsDatabase
import com.example.spotifycopy.data.remote.MusicDatabase
import com.example.spotifycopy.presentation.mapper.Mapper.toArtistsList
import com.example.spotifycopy.presentation.models.ArtistsModel
import kotlinx.coroutines.launch

class ArtistsViewModel : ViewModel() {
    private val _artists = MutableLiveData<List<ArtistsModel>>()
    val artists get() = _artists

    private val artistsDatabase = ArtistsDatabase()

    init {
        viewModelScope.launch {
            val list = artistsDatabase.getAllArtists().toArtistsList()
            _artists.value = list
        }
    }

}