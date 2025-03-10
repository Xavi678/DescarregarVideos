package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class SearchContent(val twoColumnSearchResultsRenderer: SearchContentRenderer) {
}