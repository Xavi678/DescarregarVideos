package com.ivax.descarregarvideos.ui.routes

import com.ivax.descarregarvideos.classes.RouteLabel
import kotlinx.serialization.Serializable

class Route {
    @Serializable
    @RouteLabel("Playlists")
    object Playlists
    @Serializable
    @RouteLabel("Search")
    object Search
    @Serializable
    @RouteLabel("Saved Audios")
    object SavedAudio
    @Serializable
    @RouteLabel("Edit Playlist")
    data class EditPlaylist(val playlistId: Int)
}