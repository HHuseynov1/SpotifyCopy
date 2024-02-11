package com.example.spotifycopy.view.ui.libraryFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.repo.Repository
import com.example.spotifycopy.domain.mapper.Mapper.toArtistsList
import com.example.spotifycopy.domain.mapper.Mapper.toUserToModel
import com.example.spotifycopy.domain.models.ArtistsModel
import com.example.spotifycopy.domain.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryPlaylistViewModel @Inject constructor(private val repo : Repository) : ViewModel() {
    private val _mutableLiveDataUser = MutableLiveData<List<UserModel>>()
    val mutableLiveDataUser get() = _mutableLiveDataUser

    private val _mutableLiveDataArtists = MutableLiveData<List<ArtistsModel>>()

    val mutableLiveDataArtists get() = _mutableLiveDataArtists

    init {
        viewModelScope.launch {
            val listUser = repo.getUser().toUserToModel()
            _mutableLiveDataUser.value = listUser
            val listArtists = repo.getAllArtists().toArtistsList()
            _mutableLiveDataArtists.value = listArtists
        }
    }
}