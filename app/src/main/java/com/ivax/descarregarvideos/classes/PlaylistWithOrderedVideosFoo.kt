package com.ivax.descarregarvideos.classes

import com.ivax.descarregarvideos.entities.Playlist

data class PlaylistWithOrderedVideosFoo(val playlist: Playlist,val orderedVideos : List<OrderedVideos>)

data class OrderedVideos(val position: Int,val videoId: String, val title: String, val imgUrl: String?,
                         val duration: String, val viewCount: String, var videoUrl: String?=null){

}