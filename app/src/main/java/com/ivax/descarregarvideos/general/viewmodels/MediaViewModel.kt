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
    val isMediaPlayerMaximized = mediaPlayerRepository.isMediaPlayerMaximized()
    //val isMediaPlayerVisible = mediaPlayerRepository.getMediaPlayerVisibility()

    private val _isMediaPlayerVisible=mediaPlayerRepository.getMediaPlayerVisibility()
    val isMediaPlayerVisible=_isMediaPlayerVisible.asStateFlow()
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
        _isMediaPlayerVisible.value=playing
    }

    fun minimize() {
        TODO("Not yet implemented")
    }
}

sealed class MediaStateUi {
    data class MetaDataStateUi(val playlistName: String?, val artwork: Bitmap, val title: String?) :
        MediaStateUi()
}