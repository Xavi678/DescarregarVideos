package com.ivax.descarregarvideos.repository

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaController
import com.ivax.descarregarvideos.classes.OrderedVideos
import com.ivax.descarregarvideos.classes.PlaylistWithOrderedVideosFoo
import com.ivax.descarregarvideos.classes.VideosWithPositionFoo
import com.ivax.descarregarvideos.entities.SavedVideo
import com.ivax.descarregarvideos.helpers.IMediaHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaPlayerRepository @Inject constructor(
    @ApplicationContext val context: Context,
    private val mediaHelper: IMediaHelper) {


    fun setMediaController(mediaController: MediaController) {
        mediaHelper.setMediaController(mediaController)
    }

    fun getCurrentMedia(): MutableLiveData<SavedVideo> {
        return mediaHelper.getCurrentMedia()
    }

    fun addItemMedia(mediaItem: MediaItem) {

        mediaHelper.addMediaItem(mediaItem)
    }

    fun play() {
        mediaHelper.play()
    }

    fun getMediaPlayer(): MediaController {
        return mediaHelper.getMediaPlayer()
    }

    fun setSavedVideo(savedVideo: SavedVideo) {
        mediaHelper.setSavedVideo(savedVideo)
    }

    fun isMediaPlayerMaximized(): MutableStateFlow<Boolean> {
        return mediaHelper.isMediaPlayerMaximized()
    }

    fun getMediaPlayerVisibility(): MutableStateFlow<Boolean> {
        return mediaHelper.getMediaPlayerVisibility()
    }


    fun addPlaylist(items: ArrayList<MediaItem>, playlistName: String? = null) {
        for (item in items) {
            mediaHelper.addMediaItem(item)
        }
        mediaHelper.play()
    }

    @OptIn(UnstableApi::class)
    fun SavedVideoToMediaItem(video: SavedVideo, playlistName: String? = null): MediaItem {

        val metaData = MediaMetadata.Builder()
            .setArtworkUri(video.imgUrl!!.toUri())
            .setTitle(video.title)
            .setAlbumTitle(playlistName).build()
        val mediaItem =
            MediaItem.Builder().setUri(video.videoUrl!!).setMediaMetadata(metaData)
                .setMediaId(video.videoId).build()

        return mediaItem
    }

    fun SavedVideoToMediaItem(video: OrderedVideos, playlistName: String? = null): MediaItem {
        //val uri = Uri.Builder().path(video.imgUrl).build()
        val metaData = MediaMetadata.Builder().setArtworkUri(video.imgUrl!!.toUri()).setTitle(video.title)
            .setAlbumTitle(playlistName).build()
        val mediaItem = MediaItem.Builder().setUri(video.videoUrl!!).setMediaMetadata(metaData)
            .setMediaId(video.videoId).build()
        return mediaItem
    }

    fun SavedVideoToMediaItem(
        video: VideosWithPositionFoo,
        playlistName: String? = null
    ): MediaItem {
        //val uri = Uri.Builder().path(video.imgUrl).build()
        val metaData = MediaMetadata.Builder().setArtworkUri(video.imgUrl!!.toUri()).setTitle(video.title)
            .setAlbumTitle(playlistName).build()
        val mediaItem = MediaItem.Builder().setUri(video.videoUrl!!).setMediaMetadata(metaData)
            .setMediaId(video.videoId).build()
        return mediaItem
    }


    fun addPlaylist(playlist: PlaylistWithOrderedVideosFoo, playlistName: String? = null) {
        mediaHelper.clear()
        playlist.orderedVideos.forEach {
            mediaHelper.addMediaItem(SavedVideoToMediaItem(it, playlistName))
        }
        mediaHelper.play()
    }

    fun addPlaylist(
        videosWithPositionFooList: List<VideosWithPositionFoo>,
        playlistName: String? = null
    ) {
        mediaHelper.clear()
        videosWithPositionFooList.forEach {

            mediaHelper.addMediaItem(SavedVideoToMediaItem(it, playlistName))
        }
        mediaHelper.play()
    }

    fun addPlaylistShuffle(playlist: PlaylistWithOrderedVideosFoo, playlistName: String? = null) {
        mediaHelper.clear()

        val totalVideos = playlist.orderedVideos.size - 1

        val p = arrayListOf<Int>()

        (0..totalVideos).forEach {
            p.add(it)
        }


        for (i in 0..totalVideos) {
            val randomPos = p.random()
            val it = playlist.orderedVideos.elementAt(randomPos);
            mediaHelper.addMediaItem(SavedVideoToMediaItem(it, playlistName))
            p.remove(randomPos)
        }



        mediaHelper.play()
    }

    fun addPlaylistShuffle(
        videosWithPositionFoo: List<VideosWithPositionFoo>,
        playlistName: String? = null
    ) {
        mediaHelper.clear()

        val totalVideos = videosWithPositionFoo.size - 1

        val p = arrayListOf<Int>()

        (0..totalVideos).forEach {
            p.add(it)
        }


        for (i in 0..totalVideos) {
            val randomPos = p.random()
            val it = videosWithPositionFoo.elementAt(randomPos);
            mediaHelper.addMediaItem(SavedVideoToMediaItem(it, playlistName))
            p.remove(randomPos)
        }

        mediaHelper.play()
    }


    fun clear() {
        mediaHelper.clear()
    }
}