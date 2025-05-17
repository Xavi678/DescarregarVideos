package com.ivax.descarregarvideos.ui

import androidx.lifecycle.ViewModel
import com.ivax.descarregarvideos.repository.UIRepository
import com.ivax.descarregarvideos.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val videoRepository: VideoRepository,private val uiRepository: UIRepository) : ViewModel() {
    val playlists =videoRepository.getAllPlaylists()

    val showPlaylistMenu=uiRepository.showPlaylistMenu

    fun dismissPlaylistMenu() {
        showPlaylistMenu.value=false
    }
}