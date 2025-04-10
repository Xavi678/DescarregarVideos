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
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(private val mediaPlayerRepository:MediaPlayerRepository) : ViewModel() {

    //val currentThumbnail=mediaPlayerRepository.getCurrentThumbnail()
    val isMediaVisible=mediaPlayerRepository.getCurrentMediaVisibility()
    val currentMedia=mediaPlayerRepository.getCurrentMedia()
    fun addItemMedia(mediaItem: MediaItem){
        mediaPlayerRepository.addItemMedia(mediaItem)
    }

    fun play(){
        //mediaPlayerRepository.play()
    }

    fun getMediaPlayer() : ExoPlayer{

       return mediaPlayerRepository.getMediaPlayer()
    }

}