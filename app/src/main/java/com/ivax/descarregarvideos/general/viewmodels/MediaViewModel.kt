package com.ivax.descarregarvideos.general.viewmodels

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.ivax.descarregarvideos.repository.MediaPlayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(private val mediaPlayerRepository:MediaPlayerRepository) : ViewModel() {

    fun addItemMedia(mediaItem: MediaItem){
        mediaPlayerRepository.addItemMedia(mediaItem)
    }

    fun play(){
        mediaPlayerRepository.play()
    }

    fun getMediaPlayer(){
       return mediaPlayerRepository.getMediaPlayer()
    }
}