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
    private val _artists = MutableLiveData<List<ArtistsModel>>()
    val artists get() = _artists


    init {
        viewModelScope.launch {
            val list = repo.getAllArtists().toArtistsList()
            _artists.value = list
        }
    }

}