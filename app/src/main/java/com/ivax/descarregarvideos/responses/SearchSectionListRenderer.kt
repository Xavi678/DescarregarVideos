package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class SearchSectionListRenderer(val contents: List<SearchContentSection>) {
}