package com.example.spotifycopy.view.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.presentation.mapper.Mapper.toUser
import com.example.spotifycopy.presentation.models.UserModel
import kotlinx.coroutines.launch

class NavHeaderViewModel : ViewModel() {
    private val _mutableLiveData = MutableLiveData<List<UserModel>>()
    val mutableLiveData get() = _mutableLiveData

    private val userDatabase = yCopUserDatabase()

    init {
        viewModelScope.launch {
            val list = userDatabase.getUser().toUser()
            _mutableLiveData.value = list
        }
    }

}