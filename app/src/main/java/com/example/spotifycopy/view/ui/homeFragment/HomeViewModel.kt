package com.example.spotifycopy.view.ui.homeFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.remote.SpotifyDatabase
import com.example.spotifycopy.domain.mapper.Mapper.toCartItemSong
import com.example.spotifycopy.domain.mapper.Mapper.toCartItemUser
import com.example.spotifycopy.domain.mapper.Mapper.toSongList
import com.example.spotifycopy.domain.mapper.Mapper.toUserToModel
import com.example.spotifycopy.domain.models.CartItemModel
import com.example.spotifycopy.domain.models.SongModel
import com.example.spotifycopy.domain.models.UserModel
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _mutableLiveDataUser = MutableLiveData<List<UserModel>>()
    val mutableLiveDataUser get() = _mutableLiveDataUser

    init{
        viewModelScope.launch {
            val list = SpotifyDatabase.getUser().toUserToModel()
            _mutableLiveDataUser.value = list
        }
    }

    private val _mutableLiveDataCart = MutableLiveData<List<CartItemModel>>()
    val mutableLiveDataCart get() = _mutableLiveDataCart

    init {
        viewModelScope.launch {
            val listSong = SpotifyDatabase.getAllSongs().toCartItemSong()
            val listPlaylist = SpotifyDatabase.getUser().toCartItemUser()
            _mutableLiveDataCart.value = listSong + listPlaylist
        }
    }

    private val _mutableLiveDataSong = MutableLiveData<List<SongModel>>()
    val mutableLiveDataSong get() = _mutableLiveDataSong

    init {
        viewModelScope.launch {
            val list = SpotifyDatabase.getAllSongs().toSongList()
            _mutableLiveDataSong.value = list
        }
    }
}