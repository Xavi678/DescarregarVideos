package com.ivax.descarregarvideos.ui.saved.videos

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.asLiveData
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.media3.common.MediaItem
import com.ivax.descarregarvideos.MyApplication
import com.ivax.descarregarvideos.entities.SavedVideo
import com.ivax.descarregarvideos.repository.MediaPlayerRepository
import com.ivax.descarregarvideos.repository.VideoRepository
import com.ivax.descarregarvideos.ui.search.SearchViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SavedVideosViewModel @Inject constructor(private val repository: VideoRepository, private val mediaPlayerRepository: MediaPlayerRepository) : ViewModel() {
    val allSavedVideos: LiveData<List<SavedVideo>> = repository.getAllVideos().asLiveData()

    fun addItemMedia(mediaItem: MediaItem){
        mediaPlayerRepository.addItemMedia(mediaItem)
    }

    fun play(){
        mediaPlayerRepository.play()
    }

    fun setThumbnail(bitmap: Bitmap){
        mediaPlayerRepository.setThumbnail(bitmap)
    }
}