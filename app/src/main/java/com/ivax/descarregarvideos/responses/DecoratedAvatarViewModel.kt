package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class DecoratedAvatarViewModel(val avatar: InnerAvatar) {
}