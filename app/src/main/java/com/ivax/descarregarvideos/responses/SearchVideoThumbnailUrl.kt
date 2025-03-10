package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class SearchVideoThumbnailUrl(val url: String,val width: Integer,val height: Integer) {
}