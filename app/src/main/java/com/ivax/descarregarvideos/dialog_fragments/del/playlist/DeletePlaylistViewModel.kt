package com.ivax.descarregarvideos.dialog_fragments.del.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivax.descarregarvideos.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DeletePlaylistViewModel @Inject constructor(private val videoRepository: VideoRepository) : ViewModel() {
    fun deletePlaylist(playListId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            videoRepository.deletePlaylist(playListId)
        }
    }

}