package com.ivax.descarregarvideos.dialog_fragments

import androidx.lifecycle.ViewModel
import com.ivax.descarregarvideos.entities.SavedVideo
import com.ivax.descarregarvideos.repository.MediaPlayerRepository
import com.ivax.descarregarvideos.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedVideosMenuViewModel @Inject constructor(private val videoRepository:VideoRepository) : ViewModel() {

    fun addVideoToPlaylist(playlistId :Int,videoId: String){
        videoRepository.addVideoToPlaylist(playlistId,videoId)
    }
}