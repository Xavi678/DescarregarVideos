package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class OnResponseReceivedCommands(val appendContinuationItemsAction: AppendContinuationItemsAction)
