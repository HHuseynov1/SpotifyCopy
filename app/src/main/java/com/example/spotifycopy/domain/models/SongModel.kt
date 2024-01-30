package com.example.spotifycopy.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class SongModel (
    val id:Int,
    val title:String,
    val artist:String,
    val songUrl:String,
    val imageUrl:String
) : Parcelable