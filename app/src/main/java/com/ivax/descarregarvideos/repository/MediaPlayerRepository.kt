package com.ivax.descarregarvideos.repository

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import com.ivax.descarregarvideos.entities.SavedVideo
import com.ivax.descarregarvideos.helpers.IMediaHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaPlayerRepository @Inject constructor(private val mediaHelper: IMediaHelper) {


    fun setMediaController(mediaController: MediaController){
        mediaHelper.setMediaController(mediaController)
    }
    fun getCurrentMedia(): MutableLiveData<SavedVideo> {
        return mediaHelper.getCurrentMedia()
    }
    fun addItemMedia(mediaItem: MediaItem){

        mediaHelper.addMediaItem(mediaItem)
    }
    fun play(){
        mediaHelper.play()
    }

    fun getMediaPlayer(): MediaController {
        return mediaHelper.getMediaPlayer()
    }

    fun setSavedVideo(savedVideo: SavedVideo) {
        mediaHelper.setSavedVideo(savedVideo)
    }

    fun getCurrentMediaVisibility() : MutableLiveData<Boolean> {
        return mediaHelper.getCurrentMediaVisibility()
    }

    fun addPlaylist(items: ArrayList<MediaItem>) {
        for (item in items){
            mediaHelper.addMediaItem(item)
        }
        mediaHelper.play()
    }

    fun SavedVideoToMediaItem(video: SavedVideo): MediaItem {
        val uri=Uri.Builder().path(video.imgUrl).build()
        val metaData=MediaMetadata.Builder().setArtworkUri(uri).setTitle(video.title).build()
        val mediaItem = MediaItem.Builder().setUri(video.videoUrl!!).setMediaMetadata(metaData).setMediaId(video.videoId).build()
        return mediaItem
    }
}