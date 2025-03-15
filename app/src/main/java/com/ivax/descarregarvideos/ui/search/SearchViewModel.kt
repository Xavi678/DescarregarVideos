package com.ivax.descarregarvideos.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ivax.descarregarvideos.classes.VideoItem
import com.ivax.descarregarvideos.requests.PlayerRequest
import com.ivax.descarregarvideos.responses.PlayerResponse
import domain.SearchVideosUseCase
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class SearchViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    public val searchModel= MutableStateFlow<List<VideoItem>?>(null)
    public val isLoading = MutableStateFlow<Boolean>(false)
    private val useCase = SearchVideosUseCase()
    fun SearchVideos(searchQuery: String) {
        viewModelScope.launch {
            try {
                isLoading.value=true
                val result = useCase(searchQuery)
                searchModel.value = result
            }catch (e: Exception){

            }finally{
                isLoading.value=false
            }
        }
    }

    fun downloadVideo(videoId: String) {
        this.viewModelScope.launch {
            try {

                val json = Json { // this: JsonBuilder
                    encodeDefaults = true
                    ignoreUnknownKeys = true
                }
                val client = HttpClient(CIO) {
                    install(ContentNegotiation) {
                        json(json)
                    }
                }

                val playerRequest: PlayerRequest = PlayerRequest(videoId = "U6jiXdqmUG0")
                val response: HttpResponse =
                    client.post("https://www.youtube.com/youtubei/v1/player") {
                        headers {
                            append(
                                HttpHeaders.UserAgent,
                                "com.google.ios.youtube/19.45.4 (iPhone16,2; U; CPU iOS 18_1_0 like Mac OS X; US)"
                            )
                        }
                        contentType(ContentType.Application.Json)
                        setBody(playerRequest)
                    }
                val playerResponse: PlayerResponse = response.body()
                Log.d("DescarregarVideos", playerResponse.toString())
            } catch (e: Exception) {
                e.message?.let { Log.d("DescarregarVideos", it) }
            }
            Log.d("DescarregarVide", videoId)
        }
    }

    val text: LiveData<String> = _text
}