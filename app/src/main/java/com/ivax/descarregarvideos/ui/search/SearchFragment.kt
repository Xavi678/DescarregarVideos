package com.ivax.descarregarvideos.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ivax.descarregarvideos.adapter.VideoAdapter
import com.ivax.descarregarvideos.databinding.FragmentSearchBinding
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
        val root: View = binding.root
        binding.btnSearch.setOnClickListener { view->
            var searchQuery=binding.tbxView.text.toString()
            binding.recylcerViewVideo.layoutManager =
                LinearLayoutManager(this@SearchFragment.context)
            lifecycleScope.launch {
                try {
                    searchViewModel.isLoading.collect {
                        binding.searchVideoProgressBar.isVisible = it
                    }
                    searchViewModel.SearchVideos(searchQuery)
                    searchViewModel.searchModel.collect {
                        var videoAadapter = VideoAdapter(it)

                        binding.recylcerViewVideo.adapter = videoAadapter
                    }
                }catch (e: Exception){
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}