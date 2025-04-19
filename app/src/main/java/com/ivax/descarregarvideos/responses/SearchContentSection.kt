package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class SearchContentSection(val itemSectionRenderer: SearchItemSectionRenderer?=null,
                                val continuationItemRenderer: ContinuationItemRenderer?=null) {
}