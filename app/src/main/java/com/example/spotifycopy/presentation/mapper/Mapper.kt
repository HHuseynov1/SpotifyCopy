package com.example.spotifycopy.presentation.mapper

import com.example.spotifycopy.data.entities.Artists
import com.example.spotifycopy.data.entities.Categories
import com.example.spotifycopy.data.entities.Song
import com.example.spotifycopy.data.entities.User
import com.example.spotifycopy.presentation.models.ArtistsModel
import com.example.spotifycopy.presentation.models.CartItemModel
import com.example.spotifycopy.presentation.models.CategoryModel
import com.example.spotifycopy.presentation.models.SongModel
import com.example.spotifycopy.presentation.models.UserModel

object Mapper {
    fun List<Artists>.toArtistsList() = map {
        ArtistsModel(
            it.id,
            it.imageUrl,
            it.artistName
        )
    }

    fun List<Song>.toSongList() = map {
        SongModel(
            it.id,
            it.title,
            it.artist,
            it.songUrl,
            it.imageUrl
        )
    }

    fun List<User>.toUserToModel() = map {
        UserModel(
            it.id,
            it.imgProfile,
            it.imgPlaylist,
            it.playlistName,
            it.userName
        )
    }

    fun List<Categories>.toCategoriesList() = map {
        CategoryModel(
            it.category,
            it.id
        )
    }

    fun List<User>.toCartItemUser() = map{
        CartItemModel(
            "",
            "",
            it.playlistName,
            it.imgPlaylist
        )
    }

    fun List<Song>.toCartItemSong() = map{
        CartItemModel(
            it.title,
            it.imageUrl,
            "",
            ""
        )
    }

}