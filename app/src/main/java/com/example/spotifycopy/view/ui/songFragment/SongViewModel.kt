package com.example.spotifycopy.view.ui.songFragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.entites.Song
import com.example.spotifycopy.data.remote.MusicDatabase
import kotlinx.coroutines.launch

class SongViewModel : ViewModel() {
    private val _mutableLiveData = MutableLiveData<List<Song>>()
    val mutableLiveData get() = _mutableLiveData

    private val musicDatabase = MusicDatabase()

    init {
        viewModelScope.launch {
            val list = musicDatabase.getAllSongs()
            _mutableLiveData.value = list
            Log.e("MySong", list.toString())
        }
    }
}