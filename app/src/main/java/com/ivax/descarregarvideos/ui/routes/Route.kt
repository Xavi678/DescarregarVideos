package com.ivax.descarregarvideos.ui.routes

import kotlinx.serialization.Serializable

class Route {
    @Serializable
    object Playlists
    @Serializable
    object Search
    @Serializable
    object SavedAudio
    @Serializable
    data class EditPlaylist(val playlistId: Int)
}