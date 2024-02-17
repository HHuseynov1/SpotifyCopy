package com.example.spotifycopy.view.ui.insidePlaylistFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.repo.Repository
import com.example.spotifycopy.domain.mapper.Mapper.toSongList
import com.example.spotifycopy.domain.mapper.Mapper.toUserToModel
import com.example.spotifycopy.domain.models.SongModel
import com.example.spotifycopy.domain.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsidePlaylistViewModel @Inject constructor(private val repo : Repository) : ViewModel() {
    private val _mutableLiveDataSong = MutableLiveData<List<SongModel>>()
    val mutableLiveDataSong get() = _mutableLiveDataSong

    private val _mutableLiveDataUser = MutableLiveData<List<UserModel>>()

    val mutableLiveDataUser get() = _mutableLiveDataUser

    init {
        viewModelScope.launch {
            val list = repo.getAllSongs().toSongList()
            _mutableLiveDataSong.value = list
        }
    }

    init {
        viewModelScope.launch {
            val list = repo.getUser().toUserToModel()
            _mutableLiveDataUser.value = list
        }
    }

}