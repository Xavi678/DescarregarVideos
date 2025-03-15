package com.ivax.descarregarvideos.api

import android.graphics.BitmapFactory
import android.util.Log
import com.ivax.descarregarvideos.classes.VideoItem
import com.ivax.descarregarvideos.requests.SearchRequest
import com.ivax.descarregarvideos.responses.PlayerResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
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
        val playerResponse: PlayerResponse = rsp.body()
        for (content in playerResponse.contents.twoColumnSearchResultsRenderer.primaryContents.sectionListRenderer.contents) {
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
}