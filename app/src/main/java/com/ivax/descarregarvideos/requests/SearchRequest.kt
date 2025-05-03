package com.ivax.descarregarvideos.requests

import kotlinx.serialization.Serializable

@Serializable
data class SearchRequest(
    val context: SearchContext,
    val query: String? = null,
    val continuation: String?=null
) {
}