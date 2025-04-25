package com.ivax.descarregarvideos.ui.saved.videos

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ivax.descarregarvideos.entities.SavedVideo
import com.ivax.descarregarvideos.repository.MediaPlayerRepository
import com.ivax.descarregarvideos.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SavedVideosViewModel @Inject constructor(private val repository: VideoRepository, private val mediaPlayerRepository: MediaPlayerRepository) : ViewModel() {
    val allSavedVideos: LiveData<List<SavedVideo>> = repository.getAllVideos().asLiveData()

    fun addSingleItemMedia(savedVideo: SavedVideo){
        mediaPlayerRepository.clear()
        val mediaItem=mediaPlayerRepository.SavedVideoToMediaItem(savedVideo)
        mediaPlayerRepository.addItemMedia(mediaItem)
    }

    fun play(){
        mediaPlayerRepository.play()
    }

    fun setSavedVideo(savedVideo: SavedVideo){
        mediaPlayerRepository.setSavedVideo(savedVideo)
    }

    fun setMediaVisibility(visibility: Boolean){
        mediaPlayerRepository.isMediaPlayerMaximized().postValue(visibility)
    }

    fun filterSavedVideos(savedVideoName: String): List<SavedVideo> {
        if(savedVideoName==""){
            return  allSavedVideos.value!!
        }
        return allSavedVideos.value!!.filter { it.title.lowercase().contains(savedVideoName.lowercase()) }
    }
}