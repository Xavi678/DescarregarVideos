package com.ivax.descarregarvideos.repository

import androidx.media3.common.MediaItem
import com.ivax.descarregarvideos.helpers.IMediaHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaPlayerRepository @Inject constructor(private val mediaHelper: IMediaHelper) {

    fun addItemMedia(mediaItem: MediaItem){
        mediaHelper.addMediaItem(mediaItem)
    }
    fun play(){
        mediaHelper.play()
    }
}