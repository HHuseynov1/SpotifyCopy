package com.example.spotifycopy.utils

import androidx.lifecycle.MutableLiveData
import com.example.spotifycopy.domain.models.SongModel

object CurrentMusic {
    var currentMusic=MutableLiveData<String>()
    val currentMusicLiveData= MutableLiveData<SongModel>()
}