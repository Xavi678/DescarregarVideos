package com.ivax.descarregarvideos.classes

import com.ivax.descarregarvideos.entities.Playlist
import com.ivax.descarregarvideos.entities.PlaylistSavedVideoCrossRef
import com.ivax.descarregarvideos.entities.SavedVideo
import kotlinx.serialization.Serializable

@Serializable
data class ExportData(
    val videos: List<SavedVideo>,
    val playlists: List<Playlist>,
    val videosPlaylists: List<PlaylistSavedVideoCrossRef>
) {
}