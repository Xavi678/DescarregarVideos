package com.ivax.descarregarvideos.helpers

import androidx.media3.common.MediaItem

interface IMediaHelper {
    fun addMediaItem(mediaItem: MediaItem)
    fun play()
}