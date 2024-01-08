package com.example.spotifycopy.view.ui.searchFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.remote.SpotifyDatabase
import com.example.spotifycopy.presentation.mapper.Mapper.toCategoriesList
import com.example.spotifycopy.presentation.mapper.Mapper.toUserToModel
import com.example.spotifycopy.presentation.models.CategoryModel
import com.example.spotifycopy.presentation.models.UserModel
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val _mutableLiveDataCategory = MutableLiveData<List<CategoryModel>>()
    val mutableLiveDataCategory get() = _mutableLiveDataCategory

    private val _mutableLiveDataUser = MutableLiveData<List<UserModel>>()
    val mutableLiveDataUser get() = _mutableLiveDataUser

    init {
        viewModelScope.launch {
            val list = SpotifyDatabase.getAllCategories().toCategoriesList()
            _mutableLiveDataCategory.value = list
        }
    }

    init {
        viewModelScope.launch {
            val list = SpotifyDatabase.getUser().toUserToModel()
            _mutableLiveDataUser.value = list
        }
    }
}