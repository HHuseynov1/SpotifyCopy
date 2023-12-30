package com.example.spotifycopy.presentation.mapper

import com.example.spotifycopy.data.entities.Artists
import com.example.spotifycopy.data.entities.Song
import com.example.spotifycopy.data.entities.User
import com.example.spotifycopy.presentation.models.ArtistsModel
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

    fun List<User>.toUser() = map {
        UserModel(
            it.id,
            it.imgPlaylist,
            it.imgProfile,
            it.playlistName,
            it.userName
        )
    }

}