package com.ivax.descarregarvideos.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.ivax.descarregarvideos.databinding.FragmentSearchBinding
import com.ivax.descarregarvideos.requests.SearchRequest
import com.ivax.descarregarvideos.responses.PlayerResponse
import com.ivax.descarregarvideos.ui.home.HomeViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

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
                        /*val url = "https://www.youtube.com/results?search_query=${searchQuery}"
                        val doc = Jsoup.connect(url).userAgent("Mozilla").get()
                        val inlineplayer=doc.getElementById("inline-player")*/
                        val playerResponse: PlayerResponse=rsp.body()
                        Log.d("DescarregarVides", playerResponse.toString())



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