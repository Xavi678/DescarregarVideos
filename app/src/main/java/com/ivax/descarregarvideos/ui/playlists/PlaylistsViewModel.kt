package com.ivax.descarregarvideos.ui.playlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ivax.descarregarvideos.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlaylistsViewModel @Inject constructor(private val videoRepository: VideoRepository) : ViewModel() {
    val playlists=videoRepository.getAllPlaylistsWithVideos().asLiveData()
}