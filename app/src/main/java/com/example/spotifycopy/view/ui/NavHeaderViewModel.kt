package com.example.spotifycopy.view.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.remote.SpotifyDatabase
import com.example.spotifycopy.domain.mapper.Mapper.toUserToModel
import com.example.spotifycopy.domain.models.UserModel
import kotlinx.coroutines.launch

class NavHeaderViewModel : ViewModel() {
    private val _mutableLiveData = MutableLiveData<List<UserModel>>()
    val mutableLiveData get() = _mutableLiveData

    init {
        viewModelScope.launch {
            val list = SpotifyDatabase.getUser().toUserToModel()
            _mutableLiveData.value = list
            Log.e("userModel",list.toString())
        }
    }

}