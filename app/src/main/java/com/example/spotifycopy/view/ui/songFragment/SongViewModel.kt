package com.example.spotifycopy.view.ui.songFragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.entities.Song
import com.example.spotifycopy.data.remote.MusicDatabase
import com.example.spotifycopy.presentation.mapper.Mapper.toSongList
import com.example.spotifycopy.presentation.models.SongModel
import kotlinx.coroutines.launch

class SongViewModel : ViewModel() {
    private val _mutableLiveData = MutableLiveData<List<SongModel>>()
    val mutableLiveData get() = _mutableLiveData

    private val musicDatabase = MusicDatabase()

    init {
        viewModelScope.launch {
            val list = musicDatabase.getAllSongs().toSongList()
            _mutableLiveData.value = list
        }
    }
}