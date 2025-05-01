package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class AvatarImage(val sources: ArrayList<AvatarImageSources>)
