package com.example.spotifycopy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.repo.Repository
import com.example.spotifycopy.domain.mapper.Mapper.toUserToModel
import com.example.spotifycopy.domain.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repo : Repository): ViewModel() {

    private val _isLoading = MutableLiveData(true)
    val isLoading = _isLoading

    init{
        viewModelScope.launch {
            delay(2000)
            _isLoading.value = false
        }
    }

    private val _mutableLiveData = MutableLiveData<List<UserModel>>()
    val mutableLiveData get() = _mutableLiveData

    init {
        viewModelScope.launch {
            val list = repo.getUser().toUserToModel()
            _mutableLiveData.value = list
        }
    }

}