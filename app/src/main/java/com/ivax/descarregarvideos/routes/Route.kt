package com.ivax.descarregarvideos.routes

import kotlinx.serialization.Serializable

class Route {
    @Serializable
    object Playlists
    @Serializable
    object Search
    @Serializable
    object SavedAudio
}