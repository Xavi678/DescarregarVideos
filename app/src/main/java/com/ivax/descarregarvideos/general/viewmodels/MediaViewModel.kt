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
    val isMediaPlayerVisible = mediaPlayerRepository.getMediaPlayerVisibility()
    fun setMediaController(mediaController: MediaController) {
        mediaPlayerRepository.setMediaController(mediaController)
        _isMediaControllerReady.value = true
    }

    private val _mediaStateUi = MutableStateFlow<MediaStateUi.MetaDataStateUi?>(null)
    val mediaStateUi = _mediaStateUi.asStateFlow()
    private val _isMediaControllerReady = MutableStateFlow(false)
    val isMediaControllerReady = _isMediaControllerReady.asStateFlow()
    val currentMedia = mediaPlayerRepository.getCurrentMedia()
    fun addItemMedia(mediaItem: MediaItem) {
        mediaPlayerRepository.addItemMedia(mediaItem)
    }

    private val _playlistHasPrevious: MutableStateFlow<Boolean> by lazy {
        MutableStateFlow(false)
    }
    private val _playlistHasNext: MutableStateFlow<Boolean> by lazy {
        MutableStateFlow(false)
    }
    private val _title: MutableStateFlow<String?> by lazy {
        MutableStateFlow(null)
    }
    private val _playlistName: MutableStateFlow<String?> by lazy {
        MutableStateFlow(null)
    }
    private val _thumbnail: MutableStateFlow<Bitmap?> by lazy {
        MutableStateFlow(null)
    }

    fun getMediaPlayer(): MediaController {

        return mediaPlayerRepository.getMediaPlayer()
    }

    fun setMetaData(playlistName: String?, artwork: Bitmap, title: String?) {
        _mediaStateUi.value = MediaStateUi.MetaDataStateUi(playlistName, artwork, title)
    }

    val playlistHasPrevious get() = _playlistHasPrevious
    val playlistHasNext get() = _playlistHasNext
    val title get() = _title
    val thumbnail get() = _thumbnail
    val playlistName get() = _playlistName

}

sealed class MediaStateUi {
    data class MetaDataStateUi(val playlistName: String?, val artwork: Bitmap, val title: String?) :
        MediaStateUi()
}