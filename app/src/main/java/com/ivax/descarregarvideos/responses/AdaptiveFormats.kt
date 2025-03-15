package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class AdaptiveFormats(val url: String,val mimeType: String,val quality:String,
                           val contentLength:String,val projectionType:String,
                           val qualityLabel:String?=null) {
}