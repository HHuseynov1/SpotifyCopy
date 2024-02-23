package com.example.spotifycopy.view.ui.libraryFragment.searchInLibrary

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.repo.Repository
import com.example.spotifycopy.domain.mapper.Mapper.toArtistsList
import com.example.spotifycopy.domain.mapper.Mapper.toCartItemSong
import com.example.spotifycopy.domain.mapper.Mapper.toCartItemUser
import com.example.spotifycopy.domain.mapper.Mapper.toLibraryItemArtist
import com.example.spotifycopy.domain.mapper.Mapper.toLibraryItemUser
import com.example.spotifycopy.domain.mapper.Mapper.toUserToModel
import com.example.spotifycopy.domain.models.ArtistsModel
import com.example.spotifycopy.domain.models.LibraryModel
import com.example.spotifycopy.domain.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchInLibraryViewModel @Inject constructor(private val repo : Repository)  : ViewModel() {

    private val _mutableLiveDataUser = MutableLiveData<List<UserModel>>()
    val mutableLiveDataUser get() = _mutableLiveDataUser

    private val _mutableLiveDataArtists = MutableLiveData<List<ArtistsModel>>()

    val mutableLiveDataArtists get() = _mutableLiveDataArtists

    private val _mutableLiveDataLibrary = MutableLiveData<List<LibraryModel>>()
    val mutableLiveDataLibrary get() = _mutableLiveDataLibrary

    init{
        viewModelScope.launch {
            val listUser = repo.getUser().toUserToModel()
            _mutableLiveDataUser.value = listUser
            val listArtists = repo.getAllArtists().toArtistsList()
            _mutableLiveDataArtists.value = listArtists
        }
    }

    init{
        viewModelScope.launch {
            val listSong = repo.getAllArtists().toLibraryItemArtist() as ArrayList
            val listPlaylist = repo.getUser().toLibraryItemUser() as ArrayList
            listSong.addAll(listPlaylist)
            _mutableLiveDataLibrary.value = listSong
        }
    }

}