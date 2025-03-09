package com.ivax.descarregarvideos.classes

import kotlinx.serialization.Serializable

@Serializable
data class VideoDetails(val videoId: String,val title: String,val lengthSeconds: String) {
}