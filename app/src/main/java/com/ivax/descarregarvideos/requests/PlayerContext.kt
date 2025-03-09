package com.ivax.descarregarvideos.requests

import kotlinx.serialization.Serializable

@Serializable
data class PlayerContext(val client: PlayerClient= PlayerClient()) {
}