package com.ivax.descarregarvideos.classes

import com.ivax.descarregarvideos.responses.AdaptiveFormats

data class VideoInfo(val adaptativeFormats: List<AdaptiveFormats>, val videoId: String) {
}