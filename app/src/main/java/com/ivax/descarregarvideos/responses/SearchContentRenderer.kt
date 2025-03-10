package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class SearchContentRenderer(val primaryContents: SearchPrimaryContent) {
}