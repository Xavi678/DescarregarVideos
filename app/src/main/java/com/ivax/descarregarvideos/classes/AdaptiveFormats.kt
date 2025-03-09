package com.ivax.descarregarvideos.classes

import kotlinx.serialization.Serializable

@Serializable
data class AdaptiveFormats(val url: String,val mimeType: String) {
}