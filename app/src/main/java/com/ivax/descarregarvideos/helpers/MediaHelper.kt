package com.ivax.descarregarvideos.helpers

import android.content.Context
import android.media.browse.MediaBrowser
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import javax.inject.Inject

class MediaHelper @Inject constructor(private val appContext: Context) : IMediaHelper {
    private val player = ExoPlayer.Builder(appContext).build()

    override fun play(){
        player.prepare()
        player.play()
    }

    override fun addMediaItem(mediaItem: MediaItem){
        player.setMediaItem(mediaItem)
    }

}