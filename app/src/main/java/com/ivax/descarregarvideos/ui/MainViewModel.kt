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
) : ViewModel() {
    //val playlists = videoRepository.getAllPlaylists()









    /*fun addChange(playListId: Int, videoId: String, checked: Boolean) {
        val found=changes.firstOrNull { it.videoId==videoId && it.playlistId==playListId }
        if(found!=null){
            found.checked=checked
        }else{
            changes.add(PlaylistChange(playListId,videoId,checked))
        }
    }*/

    fun deleteVideo(videoId: String) {
        videoRepository.deleteVideo(videoId)
    }
}