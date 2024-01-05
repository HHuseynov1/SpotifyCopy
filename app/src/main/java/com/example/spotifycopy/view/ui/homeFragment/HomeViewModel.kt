package com.example.spotifycopy.view.ui.homeFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.remote.SpotifyDatabase
import com.example.spotifycopy.presentation.mapper.Mapper.toUser
import com.example.spotifycopy.presentation.models.UserModel
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _mutableLiveData = MutableLiveData<List<UserModel>>()
    val mutableLiveData get() = _mutableLiveData

    init{
        viewModelScope.launch {
            val list = SpotifyDatabase.getUser().toUser()
            _mutableLiveData.value = list
        }
    }
}