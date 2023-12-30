package com.example.spotifycopy.view.ui.libraryFragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.remote.UserDatabase
import com.example.spotifycopy.presentation.mapper.Mapper.toUser
import com.example.spotifycopy.presentation.models.UserModel
import kotlinx.coroutines.launch

class LibraryPlaylistViewModel : ViewModel() {
    private val _mutableLiveData = MutableLiveData<List<UserModel>>()
    val mutableLiveData get() = _mutableLiveData

    private val userDatabase = UserDatabase()

    init {
        viewModelScope.launch {
            val list = userDatabase.getUser().toUser()
            _mutableLiveData.value = list
        }
    }
}