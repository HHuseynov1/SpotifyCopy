package com.example.spotifycopy.view.ui.searchFragment

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spotifycopy.data.remote.CategoryDatabase
import com.example.spotifycopy.presentation.mapper.Mapper.toCategoriesList
import com.example.spotifycopy.presentation.models.CategoryModel
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val _mutableLiveData = MutableLiveData<List<CategoryModel>>()
    val mutableLiveData get() = _mutableLiveData

    private val categoryDatabase = CategoryDatabase()

    init {
        viewModelScope.launch {
            val list = categoryDatabase.getAllCategories().toCategoriesList()
            _mutableLiveData.value = list
        }
    }
}