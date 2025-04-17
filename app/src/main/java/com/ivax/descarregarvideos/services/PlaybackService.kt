package com.ivax.descarregarvideos.services

import android.content.Intent
import androidx.media3.session.MediaSession
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSessionService


class PlaybackService : MediaSessionService() {
    private var mediaSession: MediaSession? = null
    //private var player: ExoPlayer?=null
    // Create your player and media session in the onCreate lifecycle event
    override fun onCreate() {
        super.onCreate()
        val player = ExoPlayer.Builder(this).build()

        mediaSession = MediaSession.Builder(this, player).build()
    }

    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }
    override fun onTaskRemoved(rootIntent: Intent?) {
        val player = mediaSession?.player

        // Check if the player is not ready to play or there are no items in the media queue
        if (player!=null && (!player.playWhenReady || player.mediaItemCount == 0)) {
            // Stop the service
            stopSelf()
        }
    }
    // Remember to release the player and media session in onDestroy
    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }
}