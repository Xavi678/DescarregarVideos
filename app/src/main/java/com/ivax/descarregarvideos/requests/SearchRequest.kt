package com.ivax.descarregarvideos.requests

import kotlinx.serialization.Serializable

@Serializable
data class SearchRequest(val context: SearchContext= SearchContext(), val query: String) {
}