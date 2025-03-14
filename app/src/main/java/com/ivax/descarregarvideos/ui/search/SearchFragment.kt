package com.ivax.descarregarvideos.ui.search

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ivax.descarregarvideos.adapter.VideoAdapter
import com.ivax.descarregarvideos.classes.VideoItem
import com.ivax.descarregarvideos.databinding.FragmentSearchBinding
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
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

//import org.jsoup.Jsoup

//import org.openqa.selenium.chrome.ChromeDriver

//import org.openqa.selenium.WebDriver
//import org.openqa.selenium.chrome.ChromeDriver

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val searchViewModel =
            ViewModelProvider(this).get(SearchViewModel::class.java)

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.btnSearch.setOnClickListener { view->
            var searchQuery=binding.tbxView.text.toString()
            lifecycleScope.launch {
                try {
                    val json = Json { // this: JsonBuilder
                        encodeDefaults = true
                        ignoreUnknownKeys = true
                        explicitNulls=false
                    }
                    var searchRequest=SearchRequest(query = searchQuery)
                       var httpClient= HttpClient(CIO){
                           install(ContentNegotiation) {
                               json(json)
                           }
                       }
                        var rsp=httpClient.post("https://www.youtube.com/youtubei/v1/search"){
                            contentType(ContentType.Application.Json)
                            setBody(searchRequest)
                        headers{
                            append(HttpHeaders.UserAgent,"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:136.0) Gecko/20100101 Firefox/136.0")
                        }
                        }
                    searchViewModel.viewModelScope.launch {
                        /*val url = "https://www.youtube.com/results?search_query=${searchQuery}"
                        val doc = Jsoup.connect(url).userAgent("Mozilla").get()
                        val inlineplayer=doc.getElementById("inline-player")*/
                        //val rawRsp=rsp.bodyAsText()
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
                                    /*searchViewModel.viewModelScope.launch {

                                        }*/

                                }


                            }
                        }
                        var videoAadapter = VideoAdapter(videoList,searchViewModel)
                        binding.recylcerViewVideo.layoutManager =
                            LinearLayoutManager(this@SearchFragment.context)
                        binding.recylcerViewVideo.adapter = videoAadapter
                    }
                }catch (e: Exception){
                    Log.d("DescarregarVides", e.message.toString())
                }
            }
         }
        /*val textView: TextView = binding.textHome
        searchViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}