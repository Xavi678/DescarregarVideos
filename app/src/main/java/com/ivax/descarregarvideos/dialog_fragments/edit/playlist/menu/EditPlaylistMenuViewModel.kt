package com.ivax.descarregarvideos.dialog_fragments.edit.playlist.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivax.descarregarvideos.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditPlaylistMenuViewModel @Inject constructor(private val videoRepository: VideoRepository) : ViewModel() {

    fun deletePlaylist(playlistId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            videoRepository.deletePlaylist(playlistId)
        }

    }
}