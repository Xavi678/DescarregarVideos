package com.ivax.descarregarvideos.repository

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.ivax.descarregarvideos.helpers.IMediaHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaPlayerRepository @Inject constructor(private val mediaHelper: IMediaHelper) {

    fun getCurrentThumbnail(): MutableLiveData<Bitmap> {
        return mediaHelper.getCurrentThumbnail()
    }
    fun addItemMedia(mediaItem: MediaItem){
        mediaHelper.addMediaItem(mediaItem)
    }
    fun play(){
        mediaHelper.play()
    }
    fun getMediaPlayer(): ExoPlayer {
        return mediaHelper.getMediaPlayer()
    }

    fun setThumbnail(bitmap: Bitmap) {
        mediaHelper.setThumbnail(bitmap)
    }
}