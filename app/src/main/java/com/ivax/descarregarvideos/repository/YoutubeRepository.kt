package com.ivax.descarregarvideos.repository

import android.graphics.BitmapFactory
import android.util.Log
import com.ivax.descarregarvideos.classes.VideoItem
import com.ivax.descarregarvideos.requests.PlayerRequest
import com.ivax.descarregarvideos.requests.SearchRequest
import com.ivax.descarregarvideos.responses.PlayerResponse
import com.ivax.descarregarvideos.responses.SearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.prepareGet
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentLength
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readRemaining
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.readByteArray
import kotlinx.serialization.json.Json
import java.io.File
import java.net.URL

class YoutubeRepository {
    var httpClient= HttpClient(CIO){
        install(ContentNegotiation) {
            json(Json { // this: JsonBuilder
                encodeDefaults = true
                ignoreUnknownKeys = true
                explicitNulls=false
            })
        }
    }

    suspend fun Search(searchQuery: String): ArrayList<VideoItem> {
        var searchRequest=SearchRequest(query = searchQuery)
        var rsp=httpClient.post("https://www.youtube.com/youtubei/v1/search"){
            contentType(ContentType.Application.Json)
            setBody(searchRequest)
            headers{
                append(HttpHeaders.UserAgent,"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:136.0) Gecko/20100101 Firefox/136.0")
            }
        }
        var videoList = ArrayList<VideoItem>()
        val searchResponse: SearchResponse = rsp.body()
        for (content in searchResponse.contents.twoColumnSearchResultsRenderer.primaryContents.sectionListRenderer.contents) {
            val itemSectionR = content.itemSectionRenderer
            if (itemSectionR != null) {
                for (sectionRContent in itemSectionR.contents) {
                    if (sectionRContent.videoRenderer != null) {
                        val title =
                            sectionRContent.videoRenderer.title.runs.firstOrNull()?.text
                        val uriString =
                            sectionRContent.videoRenderer.thumbnail.thumbnails.firstOrNull()?.url
                        val viewCount=sectionRContent.videoRenderer.viewCountText.simpleText
                        val duration=sectionRContent.videoRenderer.lengthText.simpleText
                        //val imgUrl=uriString?.toUri()

                        withContext(Dispatchers.IO) {
                            try {
                                val newurl = URL(uriString);
                                val thumbnail = BitmapFactory.decodeStream(
                                    newurl.openConnection().getInputStream()
                                );
                                videoList.add(
                                    VideoItem(
                                        videoId = sectionRContent.videoRenderer.videoId,
                                        title = title,
                                        imgUrl = thumbnail,
                                        duration = duration,
                                        viewCount = viewCount
                                    )
                                )
                            } catch (e: Exception) {
                                Log.d("DescarregarVideos", e.message.toString())
                            }
                        }
                    }
                }
            }
        }
        return videoList
    }

    suspend fun GetVideoData(videoId: String): PlayerResponse {
        try {
            val playerRequest = PlayerRequest(videoId = videoId)
            val response: HttpResponse =
                httpClient.post("https://www.youtube.com/youtubei/v1/player") {
                    headers {
                        append(
                            HttpHeaders.UserAgent,
                            "com.google.ios.youtube/19.45.4 (iPhone16,2; U; CPU iOS 18_1_0 like Mac OS X; US)"
                        )
                    }
                    contentType(ContentType.Application.Json)
                    setBody(playerRequest)
                }
            val txt=response.bodyAsText()
            Log.d("DescarregarVideos", txt)
            val playerResponse: PlayerResponse = response.body()
            Log.d("DescarregarVideos", playerResponse.toString())

            return playerResponse
        }catch (e: Exception){
            Log.d("DescarregarVideos", e.message.toString())
            throw e
        }
    }
    suspend fun DownloadVideoStream(urlString: String) : ByteArray{
        try {
            var result=byteArrayOf()
            var httpClientFile= HttpClient(CIO){
                engine{
                    requestTimeout=0
                }
            }
            httpClientFile.prepareGet(urlString = urlString).execute {
                    response ->
                val length = response.contentLength()?.toFloat() ?: 0F
                var readBytes = 0
                var progress = 0
                val channel: ByteReadChannel = response.body()
                while (!channel.isClosedForRead) {
                    /*min = DEFAULT_BUFFER_SIZE.toLong()*/
                    val packet = channel.readRemaining()
                    while (!packet.exhausted()) {
                        val bytes: ByteArray = packet.readByteArray()
                        result+=bytes;
                        //file?.appendBytes(bytes)
                        readBytes += bytes.size
                    }
                }
                Log.d("DescarregarVideos","Read Bytes: $readBytes length: $length")
            }
            return result
        }catch (e: Exception){
            Log.d("DescarregarVideos",e.message.toString())
            throw e
        }
    }
}