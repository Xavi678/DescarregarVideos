package com.ivax.descarregarvideos.classes

import kotlinx.serialization.Serializable

@Serializable
data class PlayerResponse(val videoDetails: VideoDetails,val streamingData: StreamingData) {

}

