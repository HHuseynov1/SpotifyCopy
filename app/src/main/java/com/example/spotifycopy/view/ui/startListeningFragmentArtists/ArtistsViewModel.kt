package com.example.spotifycopy.view.ui.startListeningFragmentArtists

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.repo.Repository
import com.example.spotifycopy.domain.mapper.Mapper.toArtistsList
import com.example.spotifycopy.domain.models.ArtistsModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistsViewModel @Inject constructor(private val repo : Repository): ViewModel() {
    private val _mutableLiveData = MutableLiveData<List<ArtistsModel>>()
    val mutableLiveData get() = _mutableLiveData


    init {
        viewModelScope.launch {
            val list = repo.getAllArtists().toArtistsList()
            _mutableLiveData.value = list
        }
    }

}