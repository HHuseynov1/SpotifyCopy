package com.example.spotifycopy.view.ui.songFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.remote.SpotifyDatabase
import com.example.spotifycopy.presentation.mapper.Mapper.toSongList
import com.example.spotifycopy.presentation.models.SongModel
import kotlinx.coroutines.launch

class SongViewModel : ViewModel() {
    private val _mutableLiveData = MutableLiveData<List<SongModel>>()
    val mutableLiveData get() = _mutableLiveData

    init {
        viewModelScope.launch {
            val list = SpotifyDatabase.getAllSongs().toSongList()
            _mutableLiveData.value = list
        }
    }
}