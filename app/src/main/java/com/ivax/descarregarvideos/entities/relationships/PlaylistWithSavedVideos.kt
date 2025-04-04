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
        parentColumn = "playlistId",
        entityColumn = "videoId",
        associateBy = Junction(PlaylistSavedVideoCrossRef::class)
    )
    val songs: List<SavedVideo>
)

