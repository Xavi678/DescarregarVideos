package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class PlayerResponse(val videoDetails: VideoDetails,val streamingData: StreamingData) {

}

