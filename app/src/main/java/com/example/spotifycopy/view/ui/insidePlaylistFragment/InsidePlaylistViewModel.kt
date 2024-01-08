package com.example.spotifycopy.view.ui.insidePlaylistFragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.remote.SpotifyDatabase
import com.example.spotifycopy.presentation.mapper.Mapper.toSongList
import com.example.spotifycopy.presentation.mapper.Mapper.toUserToModel
import com.example.spotifycopy.presentation.models.SongModel
import com.example.spotifycopy.presentation.models.UserModel
import kotlinx.coroutines.launch

class InsidePlaylistViewModel : ViewModel() {
    private val _mutableLiveDataSong = MutableLiveData<List<SongModel>>()
    val mutableLiveDataSong get() = _mutableLiveDataSong

    private val _mutableLiveDataUser = MutableLiveData<List<UserModel>>()

    val mutableLiveDataUser get() = _mutableLiveDataUser


    init {
        viewModelScope.launch {
            val list = SpotifyDatabase.getAllSongs().toSongList()
            _mutableLiveDataSong.value = list
        }
    }

    init {
        viewModelScope.launch {
            val list = SpotifyDatabase.getUser().toUserToModel()
            _mutableLiveDataUser.value = list
        }
    }

}