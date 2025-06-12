package com.ivax.descarregarvideos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivax.descarregarvideos.classes.PlaylistChange
import com.ivax.descarregarvideos.entities.Playlist
import com.ivax.descarregarvideos.entities.PlaylistSavedVideoCrossRef
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
    //val playlists = videoRepository.getAllPlaylists()
    val changes= mutableListOf<PlaylistChange>()
    val playlists=videoRepository.getAllPlaylistsWithVideos()
    private val _showPlaylistMenu = uiRepository.showPlaylistMenu
    private val _videoId = uiRepository.videoId
    val showPlaylistMenu=_showPlaylistMenu.asStateFlow()
    val videoId = _videoId.asStateFlow()
    private val _showCreatePlaylistMenu=MutableStateFlow(false)
    val showCreatePlaylistMenu=_showCreatePlaylistMenu.asStateFlow()

    fun dismissPlaylistMenu() {
        _showPlaylistMenu.value = false
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

    fun saveChanges() {
        viewModelScope.launch(Dispatchers.IO) {
           val adds= changes.filter { it.checked }
            adds.forEach {
                videoRepository.addPlaylistSavedVideo(PlaylistSavedVideoCrossRef(it.playlistId,it.videoId))
            }
           val removes= changes.filter { !it.checked }
            removes.forEach {
                videoRepository.deletePlaylistSavedVideo(it.playlistId,it.videoId)
            }
            dismissPlaylistMenu()
        }
    }

    fun addChange(playListId: Int, videoId: String, checked: Boolean) {
        val found=changes.firstOrNull { it.videoId==videoId && it.playlistId==playListId }
        if(found!=null){
            found.checked=checked
        }else{
            changes.add(PlaylistChange(playListId,videoId,checked))
        }
    }

    fun deleteVideo(videoId: String) {
        videoRepository.deleteVideo(videoId)
    }
}