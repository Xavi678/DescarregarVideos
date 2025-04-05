package com.ivax.descarregarvideos.entities.relationships

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.ivax.descarregarvideos.entities.Playlist
import com.ivax.descarregarvideos.entities.PlaylistSavedVideoCrossRef
import com.ivax.descarregarvideos.entities.SavedVideo

data class SavedVideoWithPlaylists(
    @Embedded val savedVideo: SavedVideo,
    @Relation(
        parentColumn = "videoId",
        entityColumn = "playlistId",
        associateBy = Junction(PlaylistSavedVideoCrossRef::class)
    )
    val playlists: List<Playlist>
)