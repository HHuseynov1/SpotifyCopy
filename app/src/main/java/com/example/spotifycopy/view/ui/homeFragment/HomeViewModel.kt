package com.example.spotifycopy.view.ui.homeFragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.repo.Repository
import com.example.spotifycopy.domain.mapper.Mapper.toCartItemSong
import com.example.spotifycopy.domain.mapper.Mapper.toCartItemUser
import com.example.spotifycopy.domain.mapper.Mapper.toSongList
import com.example.spotifycopy.domain.mapper.Mapper.toUserToModel
import com.example.spotifycopy.domain.models.CartItemModel
import com.example.spotifycopy.domain.models.SongModel
import com.example.spotifycopy.domain.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo : Repository) : ViewModel() {
    private val _mutableLiveDataUser = MutableLiveData<List<UserModel>>()
    val mutableLiveDataUser get() = _mutableLiveDataUser

    init{
        viewModelScope.launch {
            val list = repo.getUser().toUserToModel()
            _mutableLiveDataUser.value = list
        }
    }

    private val _mutableLiveDataCart = MutableLiveData<List<CartItemModel>>()
    val mutableLiveDataCart get() = _mutableLiveDataCart

    init {
        viewModelScope.launch {
            val listSong = repo.getAllSongs().toCartItemSong() as ArrayList
            val listPlaylist = repo.getUser().toCartItemUser() as ArrayList
            listSong.addAll(listPlaylist)
            _mutableLiveDataCart.value = listSong
            Log.e("adga",listSong.toString())
        }
    }

    private val _mutableLiveDataSong = MutableLiveData<List<SongModel>>()
    val mutableLiveDataSong get() = _mutableLiveDataSong

    init {
        viewModelScope.launch {
            val list = repo.getAllSongs().toSongList()
            _mutableLiveDataSong.value = list
        }
    }
}