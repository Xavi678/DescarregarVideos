package com.ivax.descarregarvideos.helpers

import android.graphics.Bitmap
import android.media.MediaPlayer
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import com.ivax.descarregarvideos.entities.SavedVideo

interface IMediaHelper {
    fun addMediaItem(mediaItem: MediaItem)
    fun play()
    fun getMediaPlayer() : MediaController
    //fun setThumbnail(bitmap: Bitmap)
    //fun getCurrentThumbnail() : MutableLiveData<Bitmap>
    fun getCurrentMedia() : MutableLiveData<SavedVideo>
    fun setSavedVideo(video: com.ivax.descarregarvideos.entities.SavedVideo)
    fun getCurrentMediaVisibility() : MutableLiveData<Boolean>
    fun setMediaController(mediaController :MediaController)
}