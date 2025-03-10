package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class SearchVideoThumbnail(val thumbnails:List<SearchVideoThumbnailUrl>) {
}