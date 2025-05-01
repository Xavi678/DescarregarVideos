package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class AvatarImageSources(val url: String,val height: Int, val width: Int)
