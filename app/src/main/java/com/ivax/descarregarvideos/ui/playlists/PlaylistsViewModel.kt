package com.ivax.descarregarvideos.ui.playlists

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.ivax.descarregarvideos.classes.OrderedVideos
import com.ivax.descarregarvideos.classes.PlaylistWithOrderedVideosFoo
import com.ivax.descarregarvideos.entities.relationships.PlaylistWithSavedVideos
import com.ivax.descarregarvideos.repository.MediaPlayerRepository
import com.ivax.descarregarvideos.repository.VideoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistsViewModel @Inject constructor(private val videoRepository: VideoRepository,private val mediaPlayerRepository: MediaPlayerRepository) : ViewModel() {

    //val playlists=videoRepository.getAllPlaylistsWithVideos().asLiveData()
    val playlists= videoRepository.getAllPlaylists()
    val orderedPlaylist : MutableStateFlow<ArrayList<PlaylistWithOrderedVideosFoo>> by lazy {
        MutableStateFlow<ArrayList<PlaylistWithOrderedVideosFoo>>(ArrayList<PlaylistWithOrderedVideosFoo>())
    }
    init {
        viewModelScope.launch(Dispatchers.IO) {
            playlists.collectLatest {
                val tmp=ArrayList<PlaylistWithOrderedVideosFoo>()
                it.forEach { x->
                    val playlistsWithPos= videoRepository.getByPlaylistIdWithPositions(x.playListId)
                    var listOrderedVideos=ArrayList<OrderedVideos>()
                    playlistsWithPos.forEach { y->
                        listOrderedVideos.add(OrderedVideos(videoId = y.videoId, viewCount = y.viewCount,
                            position = y.position, imgUrl = y.imgUrl, videoUrl = y.videoUrl,
                            title = y.title, duration = y.duration))
                    }
                    tmp.add(PlaylistWithOrderedVideosFoo(playlist = x, orderedVideos = listOrderedVideos))
                }
                orderedPlaylist.update {
                    tmp
                }
            }
        }


    }
    fun addPlaylist(playlist: PlaylistWithSavedVideos) {
        var mediaItems = ArrayList<MediaItem>()
        playlist.videos.forEach {
            val mediaItem=mediaPlayerRepository.SavedVideoToMediaItem(it,playlist.playlist.name)

            //val mediaItem = MediaItem.fromUri(it.videoUrl!!)
            //mediaItem.mediaId=
            mediaItems.add(mediaItem)
        }
        mediaPlayerRepository.addPlaylist(mediaItems)
    }
    fun setMediaVisibility(visibility: Boolean){

        mediaPlayerRepository.getCurrentMediaVisibility().postValue(visibility)
    }

    fun filterPlaylist(playlistName: String): List<PlaylistWithSavedVideos> {
        return emptyList()
        /*if(playlistName==""){
            return  playlists.value!!
        }
       return playlists.value!!.filter { it.playlist.name!!.lowercase().contains( playlistName.lowercase()) }*/
    }
    fun shuffle(playlist: PlaylistWithOrderedVideosFoo) {
        mediaPlayerRepository.addPlaylistShuffle(playlist)


    }
    fun playAll(playlist: PlaylistWithOrderedVideosFoo) {
        mediaPlayerRepository.addPlaylist(playlist)
    }

    fun getFirstVideo(playlistId: kotlin.Int) {
        viewModelScope.launch(Dispatchers.IO) {
           var firstVideo= videoRepository.getFirstVideo(playlistId)
            if(firstVideo!=null){
                Log.d("DescarregarVideos",firstVideo.title)
            }
        }

    }
}