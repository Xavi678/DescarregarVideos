package com.ivax.descarregarvideos.helpers

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.browse.MediaBrowser
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.ivax.descarregarvideos.entities.SavedVideo
import javax.inject.Inject

class MediaHelper @Inject constructor(private val appContext: Context) : IMediaHelper {
    private var player : ExoPlayer = ExoPlayer.Builder(appContext).build()
    private val actualVideo : MutableLiveData<SavedVideo> by lazy {
        MutableLiveData<SavedVideo>()
    }
    private val actualVisibility : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    override fun play(){
        player.prepare()
        player.play()
    }

    override fun addMediaItem(mediaItem: MediaItem){
        player.addMediaItem(mediaItem)
        //player.setMediaItem(mediaItem)
    }

    override fun getMediaPlayer(): ExoPlayer {
        return player
    }

    /*override fun setThumbnail(bitmap: Bitmap)  {
        actualVideo.
        acutalThumbnail.postValue(bitmap)
    }

    override fun getCurrentThumbnail(): MutableLiveData<Bitmap> {
       return acutalThumbnail
    }*/
    override fun setSavedVideo(video: SavedVideo) {
        actualVideo.postValue(video)
    }

    override fun getCurrentMediaVisibility(): MutableLiveData<Boolean> {
        return actualVisibility
    }

    override  fun getCurrentMedia(): MutableLiveData<SavedVideo> {
        return actualVideo
    }


}