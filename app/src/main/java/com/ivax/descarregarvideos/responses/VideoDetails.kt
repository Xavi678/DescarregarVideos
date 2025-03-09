package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class VideoDetails(val videoId: String,val title: String,val lengthSeconds: String,val shortDescription: String) {
}