package com.example.spotifycopy.view.ui.songFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.repo.Repository
import com.example.spotifycopy.domain.mapper.Mapper.toSongList
import com.example.spotifycopy.domain.models.SongModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SongViewModel @Inject constructor(private val repo : Repository) : ViewModel() {
    private val _mutableLiveData = MutableLiveData<List<SongModel>>()
    val mutableLiveData get() = _mutableLiveData

    init {
        viewModelScope.launch {
            val list = repo.getAllSongs().toSongList()
            _mutableLiveData.value = list
        }
    }
}