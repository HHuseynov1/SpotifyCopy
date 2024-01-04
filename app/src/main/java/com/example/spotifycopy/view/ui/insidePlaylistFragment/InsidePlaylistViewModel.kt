package com.example.spotifycopy.view.ui.insidePlaylistFragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.entities.Song
import com.example.spotifycopy.data.remote.MusicDatabase
import com.example.spotifycopy.data.remote.UserDatabase
import com.example.spotifycopy.presentation.mapper.Mapper.toSongList
import com.example.spotifycopy.presentation.mapper.Mapper.toUser
import com.example.spotifycopy.presentation.models.SongModel
import com.example.spotifycopy.presentation.models.UserModel
import kotlinx.coroutines.launch

class InsidePlaylistViewModel : ViewModel() {
    private val _mutableLiveDataSong = MutableLiveData<List<SongModel>>()

    private val _mutableLiveDataUser = MutableLiveData<List<UserModel>>()

    val mutableLiveDataSong get() = _mutableLiveDataSong

    val mutableLiveDataUser get() = _mutableLiveDataUser


    private val musicDatabase = MusicDatabase()

    init {
        viewModelScope.launch {
            val list = musicDatabase.getAllSongs().toSongList()
            _mutableLiveDataSong.value = list
        }
    }

    private val userDatabase = UserDatabase()

    init {
        viewModelScope.launch {
            val list = userDatabase.getUser().toUser()
            _mutableLiveDataUser.value = list
        }
    }

}