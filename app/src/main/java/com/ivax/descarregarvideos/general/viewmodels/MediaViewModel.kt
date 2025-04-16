package com.ivax.descarregarvideos.general.viewmodels


import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.ivax.descarregarvideos.entities.SavedVideo
import com.ivax.descarregarvideos.repository.MediaPlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(private val mediaPlayerRepository:MediaPlayerRepository) : ViewModel() {

    //val currentThumbnail=mediaPlayerRepository.getCurrentThumbnail()
    val isMediaVisible=mediaPlayerRepository.getCurrentMediaVisibility()

    val currentMedia=mediaPlayerRepository.getCurrentMedia()
    fun addItemMedia(mediaItem: MediaItem){
        mediaPlayerRepository.addItemMedia(mediaItem)
    }
    private val _playlistHasPrevious : MutableStateFlow<Boolean> by lazy {
        MutableStateFlow(false)
    }
    private val _playlistHasNext : MutableStateFlow<Boolean> by lazy {
        MutableStateFlow(false)
    }
    private val _title : MutableStateFlow<String?> by lazy{
        MutableStateFlow(null)
    }
    private val _thumbnail : MutableStateFlow<Bitmap?> by lazy{
        MutableStateFlow(null)
    }

    fun getMediaPlayer() : ExoPlayer{

       return mediaPlayerRepository.getMediaPlayer()
    }
    val playlistHasPrevious get() =_playlistHasPrevious
    val playlistHasNext get() =_playlistHasNext
    val title get()= _title
    val thumbnail get()=_thumbnail

}