package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponseContinuation(val onResponseReceivedCommands: ArrayList<OnResponseReceivedCommands>) {
}