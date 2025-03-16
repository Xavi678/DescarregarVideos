package com.ivax.descarregarvideos.ui.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ivax.descarregarvideos.adapter.VideoAdapter
import com.ivax.descarregarvideos.databinding.FragmentSearchBinding
import com.ivax.descarregarvideos.dialog_fragments.CodecsConfirmDialogFragment
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest

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
        lifecycleScope.launch {
            searchViewModel.bytes.collectLatest { latest ->
                if(latest!=null) {
                    activity?.applicationContext?.openFileOutput("prova.mp4", Context.MODE_PRIVATE)
                        .use {

                            it?.write(latest)
                        }
                }
            }
        }
        searchViewModel.adaptativeFormats.observe(viewLifecycleOwner, Observer {
            if(it!=null) {
                CodecsConfirmDialogFragment(it, itemClickListener = fun(url: String?){
                    searchViewModel.downloadVideoStream(url)
                }).show(
                    childFragmentManager, CodecsConfirmDialogFragment.TAG
                )
            }
        })
        val root: View = binding.root
        binding.btnSearch.setOnClickListener { view ->
            var searchQuery = binding.tbxView.text.toString()
            binding.recylcerViewVideo.layoutManager =
                LinearLayoutManager(this@SearchFragment.context)
            lifecycleScope.launch {
                searchViewModel.isLoading.collectLatest {
                    binding.searchVideoProgressBar.isVisible = it
                }
            }
            lifecycleScope.launch {
                try {

                    searchViewModel.SearchVideos(searchQuery)
                    searchViewModel.searchModel.collectLatest {
                        var videoAadapter = VideoAdapter(
                            it,
                            itemClickListener = fun(videoId: String) {
                                lifecycleScope.launch {
                                    searchViewModel.downloadVideoResponse(videoId)
                                }
                                Log.d("DescarregarVideo", videoId)
                            }
                        )

                        binding.recylcerViewVideo.adapter = videoAadapter
                    }
                } catch (e: Exception) {
                    e.message.toString().let {
                        Log.d("DescarregarVideos", it)
                    }
                }
            }

        }
        /*val textView: TextView = binding.textHome
        searchViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    fun downloadButton(videoId: String) {
        Log.d("DescarregarVideo", videoId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}