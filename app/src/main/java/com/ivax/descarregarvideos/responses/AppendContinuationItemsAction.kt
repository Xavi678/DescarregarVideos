package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class AppendContinuationItemsAction(val continuationItems: ArrayList<ContinuationItem>)
