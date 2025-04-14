package com.ivax.descarregarvideos.entities.relationships

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.ivax.descarregarvideos.entities.Playlist
import com.ivax.descarregarvideos.entities.PlaylistSavedVideoCrossRef
import com.ivax.descarregarvideos.entities.SavedVideo

data class PlaylistWithSavedVideos(
    @Embedded val playlist: Playlist,
    @Relation(
        parentColumn = "playListId",
        entityColumn = "videoId",
        associateBy = Junction(PlaylistSavedVideoCrossRef::class)

    )
    val videos: List<SavedVideo>
)

