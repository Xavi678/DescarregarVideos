package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class SearchVideoRenderer(val videoId: String,
                               val thumbnail: SearchVideoThumbnail,
                               val title: SearchVideoTitle,
                               val lengthText: SearchVideoRendererLength,
    val viewCountText: SearchVideoRendererViewCount) {
}