package com.ivax.descarregarvideos.helpers

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.media3.common.MediaItem
import androidx.media3.session.MediaController
import com.ivax.descarregarvideos.entities.SavedVideo
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MediaHelper @Inject constructor(private val appContext: Context) : IMediaHelper {

    private lateinit var mediaController : MediaController
    //private var player : ExoPlayer = mediaController
    override fun setMediaController(mediaController :MediaController){
        this.mediaController=mediaController
    }
    private val actualVideo : MutableLiveData<SavedVideo> by lazy {
        MutableLiveData<SavedVideo>()
    }
    private val isMaximized : MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    private val isMediaPlayerVisible : MutableStateFlow<Boolean> by lazy {
        MutableStateFlow<Boolean>(false)
    }


    override fun play(){
        mediaController.prepare()
        mediaController.play()
        isMediaPlayerVisible.value=true
    }
    override fun clear(){
        if(mediaController.mediaItemCount>0){
            mediaController.clearMediaItems()
        }
    }
    override fun setShuffle(){
        mediaController.shuffleModeEnabled=true
    }
    override fun addMediaItemList(mediaItem: List<MediaItem>){
        mediaController.addMediaItems(mediaItem)
        //player.setMediaItem(mediaItem)
    }
    override fun addMediaItem(mediaItem: MediaItem){
        mediaController.addMediaItem(mediaItem)
        //player.setMediaItem(mediaItem)
    }

    override fun getMediaPlayer(): MediaController {
        return mediaController
    }

    /*override fun setThumbnail(bitmap: Bitmap)  {
        actualVideo.
        acutalThumbnail.postValue(bitmap)
    }

    override fun getCurrentThumbnail(): MutableLiveData<Bitmap> {
       return acutalThumbnail
    }*/
    override fun setSavedVideo(video: SavedVideo) {
        actualVideo.postValue(video)
    }

    override fun isMediaPlayerMaximized(): MutableLiveData<Boolean> {
        return isMaximized
    }

    override fun getMediaPlayerVisibility() : MutableStateFlow<Boolean>{
        return isMediaPlayerVisible
    }

    override  fun getCurrentMedia(): MutableLiveData<SavedVideo> {
        return actualVideo
    }


}