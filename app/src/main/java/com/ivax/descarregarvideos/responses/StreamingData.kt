package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class StreamingData(val expiresInSeconds: String,val adaptiveFormats:List<AdaptiveFormats>) {
}