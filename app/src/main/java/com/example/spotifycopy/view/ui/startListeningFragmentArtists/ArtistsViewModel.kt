package com.example.spotifycopy.view.ui.startListeningFragmentArtists

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.remote.SpotifyDatabase
import com.example.spotifycopy.presentation.mapper.Mapper.toArtistsList
import com.example.spotifycopy.presentation.models.ArtistsModel
import kotlinx.coroutines.launch

class ArtistsViewModel : ViewModel() {
    private val _artists = MutableLiveData<List<ArtistsModel>>()
    val artists get() = _artists


    init {
        viewModelScope.launch {
            val list = SpotifyDatabase.getAllArtists().toArtistsList()
            _artists.value = list
        }
    }

}