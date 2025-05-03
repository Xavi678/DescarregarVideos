package com.ivax.descarregarvideos.repository

import android.graphics.BitmapFactory
import android.util.Log
import com.ivax.descarregarvideos.classes.SearchResponseFoo
import com.ivax.descarregarvideos.classes.VideoItem
import com.ivax.descarregarvideos.helpers.IApiClient
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
import javax.inject.Inject

class YoutubeRepository @Inject constructor(private val apiClient : IApiClient) {


    suspend fun Search(searchQuery: String): SearchResponseFoo{
        return apiClient.Search(searchQuery)
    }

    suspend fun SearchMore( token: String): SearchResponseFoo{
        return apiClient.SearchMoreVideos(token)
    }

    suspend fun GetVideoData(videoId: String): PlayerResponse {
        return apiClient.GetVideoData(videoId)
    }
    suspend fun DownloadVideoStream(urlString: String) : ByteArray{
        return apiClient.DownloadVideoStream(urlString)
    }
}