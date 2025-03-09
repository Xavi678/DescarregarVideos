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
import com.ivax.descarregarvideos.ui.home.HomeViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import kotlinx.coroutines.launch

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
            var searchQuery=binding.tbxView.text
            lifecycleScope.launch {
                val client = HttpClient(CIO)
                val rsp=client.get("https://www.youtube.com/results?search_query=${searchQuery}"){
                    headers{
                        append(
                            HttpHeaders.UserAgent,
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:136.0) Gecko/20100101 Firefox/136.0"
                        )
                    }
                }
                Log.d("DescarregarVides",rsp.body())
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