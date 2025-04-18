package com.ivax.descarregarvideos.helpers

import com.ivax.descarregarvideos.classes.VideoItem
import com.ivax.descarregarvideos.responses.PlayerResponse

interface IApiClient {
   suspend fun Search(searchQuery: String): ArrayList<VideoItem>
   suspend fun GetVideoData(videoId: String): PlayerResponse
   suspend fun DownloadVideoStream(urlString: String) : ByteArray
}