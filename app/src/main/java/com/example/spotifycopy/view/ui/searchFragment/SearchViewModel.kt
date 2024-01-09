package com.example.spotifycopy.view.ui.searchFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.repo.Repository
import com.example.spotifycopy.domain.mapper.Mapper.toCategoriesList
import com.example.spotifycopy.domain.mapper.Mapper.toUserToModel
import com.example.spotifycopy.domain.models.CategoryModel
import com.example.spotifycopy.domain.models.UserModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class SearchViewModel @Inject constructor(private val repo : Repository) : ViewModel() {
    private val _mutableLiveDataCategory = MutableLiveData<List<CategoryModel>>()
    val mutableLiveDataCategory get() = _mutableLiveDataCategory

    private val _mutableLiveDataUser = MutableLiveData<List<UserModel>>()
    val mutableLiveDataUser get() = _mutableLiveDataUser

    init {
        viewModelScope.launch {
            val list = repo.getAllCategories().toCategoriesList()
            _mutableLiveDataCategory.value = list
        }
    }

    init {
        viewModelScope.launch {
            val list = repo.getUser().toUserToModel()
            _mutableLiveDataUser.value = list
        }
    }
}