package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class SearchItemSectionRendererContent(val videoRenderer:SearchVideoRenderer?=null) {
}