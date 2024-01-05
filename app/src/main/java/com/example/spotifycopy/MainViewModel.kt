package com.example.spotifycopy

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _isLoading = MutableLiveData(true)
    val isLoading = _isLoading

    init{
        viewModelScope.launch {
            delay(2000)
            _isLoading.value = false
        }
    }

}