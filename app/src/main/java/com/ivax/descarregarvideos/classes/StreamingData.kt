package com.ivax.descarregarvideos.classes

import kotlinx.serialization.Serializable

@Serializable
data class StreamingData(val expiresInSeconds: String,val adaptiveFormats:List<AdaptiveFormats>) {
}