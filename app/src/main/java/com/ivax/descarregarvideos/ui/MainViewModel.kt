package com.ivax.descarregarvideos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivax.descarregarvideos.entities.Playlist
import com.ivax.descarregarvideos.repository.UIRepository
import com.ivax.descarregarvideos.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val videoRepository: VideoRepository,
    private val uiRepository: UIRepository
) : ViewModel() {
    val playlists = videoRepository.getAllPlaylists()

    val showPlaylistMenu = uiRepository.showPlaylistMenu
    private val _showCreatePlaylistMenu=MutableStateFlow(false)
    val showCreatePlaylistMenu=_showCreatePlaylistMenu.asStateFlow()

    fun dismissPlaylistMenu() {
        showPlaylistMenu.value = false
    }

    fun createPlaylist(playlistName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val playlist = Playlist(0, name = playlistName)
            videoRepository.addPlaylist(playlist)
            dismissCreatePlaylistMenu()
        }
    }

    fun dismissCreatePlaylistMenu() {
        _showCreatePlaylistMenu.value=false
    }


    fun showCreatePlaylistMenu(){
        _showCreatePlaylistMenu.value=true
    }
}