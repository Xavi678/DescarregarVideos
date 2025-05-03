package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class ContinuationItem(val itemSectionRenderer: SearchItemSectionRenderer?=null, val continuationItemRenderer: ContinuationItemRenderer?=null)
