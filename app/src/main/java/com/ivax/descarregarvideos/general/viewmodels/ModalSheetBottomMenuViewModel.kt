package com.ivax.descarregarvideos.general.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivax.descarregarvideos.classes.PlaylistChange
import com.ivax.descarregarvideos.entities.Playlist
import com.ivax.descarregarvideos.entities.PlaylistSavedVideoCrossRef
import com.ivax.descarregarvideos.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModalSheetBottomMenuViewModel @Inject constructor(private val videoRepository: VideoRepository) : ViewModel() {

    private val _isBottomSheetVisible = MutableStateFlow<Boolean>(false)
    private val _bottomSheetParameter = MutableStateFlow<String?>(null)
    private val _showPlaylistMenu= MutableStateFlow<Boolean>(false)
    private val _selectedVideoId= MutableStateFlow<String?>(null)
    val playlists=videoRepository.getAllPlaylistsWithVideos()
    private val _showCreatePlaylistMenu=MutableStateFlow(false)
    val showCreatePlaylistMenu=_showCreatePlaylistMenu.asStateFlow()
    private val _closeMenu =MutableStateFlow(false)

    val closeMenu=_closeMenu.asStateFlow()

    val changes= mutableListOf<PlaylistChange>()

    val bottomSheetParameter = _bottomSheetParameter.asStateFlow()
    val showPlaylistMenu=_showPlaylistMenu.asStateFlow()
    val selectedVideoId=_selectedVideoId.asStateFlow()

    fun deleteVideo(videoId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            videoRepository.deleteVideo(videoId)
            close()
        }
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

    val isBottomSheetVisible: StateFlow<Boolean> = _isBottomSheetVisible.asStateFlow()

    fun setBottomSheetVisibility(state: Boolean) {
        _isBottomSheetVisible.value = state
    }

    fun setBottomSheetVideoId(videoId: String) {
        _bottomSheetParameter.value = videoId
    }

    fun showPlaylistMenu(showMenu: Boolean){
        _showPlaylistMenu.value=showMenu
    }
    fun setSelectedVideoId(videoId : String){
        _selectedVideoId.value=videoId
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
        }
    }

    fun dismissPlaylistMenu() {
        _showCreatePlaylistMenu.value=false
    }
    fun addChange(playListId: Int, videoId: String, checked: Boolean) {
        val found=changes.firstOrNull { it.videoId==videoId && it.playlistId==playListId }
        if(found!=null){
            found.checked=checked
        }else{
            changes.add(PlaylistChange(playListId,videoId,checked))
        }
    }

    fun close() {
        _closeMenu.value=true
    }

    fun deleteVideoFromPlaylist(selectedVideoId: String, playlistId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            videoRepository.deletePlaylistSavedVideo(playlistId,selectedVideoId)
        }

    }

}