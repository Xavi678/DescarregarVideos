package com.ivax.descarregarvideos.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.ivax.descarregarvideos.adapter.VideoAdapter
import com.ivax.descarregarvideos.databinding.FragmentSearchBinding
import com.ivax.descarregarvideos.entities.SavedVideo
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
        adapter= VideoAdapter(itemClickListener = fun(saveVideo: SavedVideo, finished: ()->Unit) {
                searchViewModel.downloadVideoResponse(saveVideo,finished)

            })
        val root: View = binding.root
        binding.btnSearch.setOnClickListener { view ->
            var searchQuery = binding.tbxView.text.toString()

            lifecycleScope.launch {
                searchViewModel.isLoading.collectLatest {

                    binding.searchVideoProgressBar.isVisible = it
                }
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    try {

                        searchViewModel.SearchVideos(searchQuery)
                        searchViewModel.searchModel.collect {
                            adapter.addItems(it)
                        }
                    } catch (e: Exception) {
                        e.message.toString().let {
                            Log.d("DescarregarVideos", it)
                        }
                    }
                }
            }

        }
        /*val textView: TextView = binding.textHome
        searchViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        setupUi()
        return root
    }
    private fun setupUi(){
        binding.recylcerViewVideo.layoutManager =
            LinearLayoutManager(this@SearchFragment.context)
        binding.recylcerViewVideo.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}