package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class ContinuationEndpoint(val continuationCommand : ContinuationCommand) {

}
