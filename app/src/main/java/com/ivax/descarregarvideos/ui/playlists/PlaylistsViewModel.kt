package com.ivax.descarregarvideos.ui.playlists

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.ivax.descarregarvideos.entities.relationships.PlaylistWithSavedVideos
import com.ivax.descarregarvideos.repository.MediaPlayerRepository
import com.ivax.descarregarvideos.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlaylistsViewModel @Inject constructor(private val videoRepository: VideoRepository,private val mediaPlayerRepository: MediaPlayerRepository) : ViewModel() {

    val playlists=videoRepository.getAllPlaylistsWithVideos().asLiveData()

    fun addPlaylist(playlist: PlaylistWithSavedVideos) {
        var mediaItems = ArrayList<MediaItem>()
        playlist.videos.forEach {
            val mediaItem=mediaPlayerRepository.SavedVideoToMediaItem(it,playlist.playlist.name)

            //val mediaItem = MediaItem.fromUri(it.videoUrl!!)
            //mediaItem.mediaId=
            mediaItems.add(mediaItem)
        }
        mediaPlayerRepository.addPlaylist(mediaItems)
    }
    fun setMediaVisibility(visibility: Boolean){

        mediaPlayerRepository.getCurrentMediaVisibility().postValue(visibility)
    }
}