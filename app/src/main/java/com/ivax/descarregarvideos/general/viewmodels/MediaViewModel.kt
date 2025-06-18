package com.ivax.descarregarvideos.general.viewmodels


import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import com.ivax.descarregarvideos.repository.MediaPlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(private val mediaPlayerRepository: MediaPlayerRepository) :
    ViewModel() {

    //val currentThumbnail=mediaPlayerRepository.getCurrentThumbnail()
    //val isMediaPlayerVisible = mediaPlayerRepository.getMediaPlayerVisibility()
    private val _isPlaying=MutableStateFlow(false)
    val isPlaying=_isPlaying.asStateFlow()
    private val _isMediaPlayerVisible = mediaPlayerRepository.getMediaPlayerVisibility()
    val isMediaPlayerVisible = _isMediaPlayerVisible.asStateFlow()

    private val _isMediaPlayerMaximized = mediaPlayerRepository.isMediaPlayerMaximized()
    val isMediaPlayerMaximized = _isMediaPlayerMaximized.asStateFlow()

    private val _mediaStateUi = MutableStateFlow<MediaStateUi.MetaDataStateUi?>(null)
    val mediaStateUi = _mediaStateUi.asStateFlow()
    private val _isMediaControllerReady = MutableStateFlow(false)
    val isMediaControllerReady = _isMediaControllerReady.asStateFlow()
    val currentMedia = mediaPlayerRepository.getCurrentMedia()

    fun addItemMedia(mediaItem: MediaItem) {
        mediaPlayerRepository.addItemMedia(mediaItem)
    }


    fun setMediaController(mediaController: MediaController) {
        mediaPlayerRepository.setMediaController(mediaController)
        _isMediaControllerReady.value = true
    }

    fun getMediaPlayer(): MediaController {

        return mediaPlayerRepository.getMediaPlayer()
    }

    fun setMetaData(playlistName: String?, artwork: Bitmap, title: String?) {
        _mediaStateUi.value = MediaStateUi.MetaDataStateUi(playlistName, artwork, title)
    }

    fun setVisibility(playing: Boolean) {
        _isMediaPlayerVisible.value = playing
    }

    fun minimize() {
        _isMediaPlayerMaximized.value=false
    }

    fun maximize() {
        _isMediaPlayerMaximized.value=true
    }

    fun setPlaying(playing: Boolean) {
        _isPlaying.value=playing
    }
}

sealed class MediaStateUi {
    data class MetaDataStateUi(val playlistName: String?, val artwork: Bitmap, val title: String?) :
        MediaStateUi()
}