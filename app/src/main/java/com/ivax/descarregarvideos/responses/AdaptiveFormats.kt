package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class AdaptiveFormats(val url: String,val mimeType: String,val quality:String,
                           val projectionType:String,
                           val qualityLabel:String?=null,val contentLength:String?=null) {
}