package com.ivax.descarregarvideos.requests

import kotlinx.serialization.Serializable

@Serializable
data class PlayerRequest(val videoId: String, val contentCheckout: Boolean=true, val context: PlayerContext= PlayerContext()) {
}