package com.example.spotifycopy

import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.repo.Repository
import com.example.spotifycopy.domain.mapper.Mapper.toSongList
import com.example.spotifycopy.domain.mapper.Mapper.toUserToModel
import com.example.spotifycopy.domain.models.SongModel
import com.example.spotifycopy.domain.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo : Repository): ViewModel() {

    private val _isLoading = MutableLiveData(true)
    val isLoading = _isLoading

    init{
        viewModelScope.launch {
            delay(2000)
            _isLoading.value = false
        }
    }

    private val _mutableLiveDataUser = MutableLiveData<List<UserModel>>()
    val mutableLiveData get() = _mutableLiveDataUser

    init {
        viewModelScope.launch {
            val list = repo.getUser().toUserToModel()
            _mutableLiveDataUser.value = list
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