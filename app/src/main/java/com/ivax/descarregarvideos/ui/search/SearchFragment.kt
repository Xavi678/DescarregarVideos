package com.ivax.descarregarvideos.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivax.descarregarvideos.adapter.VideoAdapter
import com.ivax.descarregarvideos.custom.composables.SearchComposable
import com.ivax.descarregarvideos.databinding.FragmentSearchBinding
import com.ivax.descarregarvideos.entities.SavedVideo
import com.ivax.descarregarvideos.ui.saved.videos.SavedVideosViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

//import org.jsoup.Jsoup

//import org.openqa.selenium.chrome.ChromeDriver

//import org.openqa.selenium.WebDriver
//import org.openqa.selenium.chrome.ChromeDriver

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private var nextToken : String?=null

    private lateinit var adapter: VideoAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val searchViewModel : SearchViewModel by lazy{
        ViewModelProvider(this)[SearchViewModel::class.java]}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.composeViewSearch.setContent {
            Column {
                SearchComposable(onClickInvoker = fun(text: String) {

                })
                SearchVideos()
            }
        }
        adapter= VideoAdapter(itemClickListener = fun(saveVideo: SavedVideo, finished: ()->Unit) {
                searchViewModel.downloadVideoResponse(saveVideo,finished)

        })
        lifecycleScope.launch {
            /*searchViewModel.currentVideos.collectLatest {
                adapter.addCurrentVideos(it)
            }*/
        }
        val root: View = binding.root
        /*binding.layoutSearchSearch.btnSearch.setOnClickListener { view ->
            var searchQuery = binding.layoutSearchSearch.tbxView.text.toString()

            lifecycleScope.launch {
                searchViewModel.isLoading.collectLatest {

                    binding.searchVideoProgressBar.isVisible = it
                }
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    try {

                        searchViewModel.SearchVideos(searchQuery)
                        searchViewModel.searchModel.collectLatest {
                            if(it!=null){
                                adapter.addItems(it.videos)
                                nextToken=it.nextToken

                            }

                        }
                    } catch (e: Exception) {
                        e.message.toString().let {
                            Log.d("DescarregarVideos", it)
                        }
                    }
                }
            }

        }*/
        /*val textView: TextView = binding.textHome
        searchViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        setupUi()
        return root
    }
    private fun setupUi(){
        /*binding.recylcerViewVideo.layoutManager =
            LinearLayoutManager(this@SearchFragment.context)
        binding.recylcerViewVideo.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            var currentPos=0
            private var loading = true
            private var previousTotalItems = 0
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                currentPos=currentPos+dy
                val linearLayout=recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = linearLayout.childCount
                val totalItemCount = linearLayout.itemCount
                val firstVisibleItemPosition = linearLayout.findFirstVisibleItemPosition()

                if (loading && totalItemCount > previousTotalItems) {
                    loading = false
                    previousTotalItems = totalItemCount
                }

                if (loading && totalItemCount > previousTotalItems) {
                    loading = false
                    previousTotalItems = totalItemCount
                }
                Log.d("DescarregarVideos","Current Pos: ${currentPos}")
                Log.d("DescarregarVideos","${recyclerView.scrollY} ${recyclerView.height} ${recyclerView.y} ${dx} ${dy}")
                val visibleThreshold = 5
                if (!loading && (totalItemCount - visibleItemCount <= firstVisibleItemPosition + visibleThreshold)) {
                    searchViewModel.SearchVideos(binding.layoutSearchSearch.tbxView.text.toString(),nextToken)
                    loading = true
                }
            }
        })
        binding.recylcerViewVideo.adapter = adapter*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    @Composable
    fun SearchVideos(
        savedVideosViewModel: SavedVideosViewModel = viewModel()
    ) {
        val savedVideos by savedVideosViewModel.allSavedVideos.collectAsStateWithLifecycle(listOf<SavedVideo>())
        LazyColumn(Modifier.fillMaxSize()) {
            items(savedVideos) {
                Item()
            }
        }
    }
    @Composable
    fun Item(){

    }
}