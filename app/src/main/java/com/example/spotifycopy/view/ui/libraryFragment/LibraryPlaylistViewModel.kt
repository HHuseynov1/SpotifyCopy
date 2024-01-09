package com.example.spotifycopy.view.ui.libraryFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.repo.Repository
import com.example.spotifycopy.domain.mapper.Mapper.toUserToModel
import com.example.spotifycopy.domain.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryPlaylistViewModel @Inject constructor(private val repo : Repository) : ViewModel() {
    private val _mutableLiveData = MutableLiveData<List<UserModel>>()
    val mutableLiveData get() = _mutableLiveData


    init {
        viewModelScope.launch {
            val list = repo.getUser().toUserToModel()
            _mutableLiveData.value = list
        }
    }
}