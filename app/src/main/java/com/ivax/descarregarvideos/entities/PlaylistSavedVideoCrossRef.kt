package com.ivax.descarregarvideos.entities

import androidx.room.Entity

@Entity(primaryKeys = ["playListId", "videoId"])
data class PlaylistSavedVideoCrossRef(
    val playListId: Int,
    val videoId: String
)