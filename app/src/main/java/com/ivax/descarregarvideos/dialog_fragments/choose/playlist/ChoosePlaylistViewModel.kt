package com.ivax.descarregarvideos.dialog_fragments.choose.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ivax.descarregarvideos.entities.Playlist
import com.ivax.descarregarvideos.entities.PlaylistSavedVideoCrossRef
import com.ivax.descarregarvideos.entities.relationships.PlaylistWithSavedVideos
import com.ivax.descarregarvideos.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChoosePlaylistViewModel @Inject constructor(private val videoRepository: VideoRepository): ViewModel() {
    fun insertSavedVideoToPlaylist(playlistSavedVideoCrossRef: PlaylistSavedVideoCrossRef) {
        viewModelScope.launch(Dispatchers.IO) {
            val listPlaylistSavedVideoCrossRef=videoRepository.getPlaylistSavedVideoCrossRefbyPlaylistId(playlistSavedVideoCrossRef.playListId)
            val last= listPlaylistSavedVideoCrossRef.maxByOrNull {
                it.position
            }
            val position: Int= (last?.position?.plus(1)) ?: 0
            playlistSavedVideoCrossRef.position=position
            videoRepository.addPlaylistSavedVideo(playlistSavedVideoCrossRef)
        }
    }

    val allPlaylists: LiveData<List<Playlist>> = videoRepository.getAllPlaylists().asLiveData()

    val allPlaylistsWithVideos: LiveData<List<PlaylistWithSavedVideos>> = videoRepository.getAllPlaylistsWithVideos().asLiveData()

}