package com.ivax.descarregarvideos.ui.saved.videos

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ivax.descarregarvideos.adapter.SavedVideosAdapter
import com.ivax.descarregarvideos.databinding.FragmentSavedVideosBinding
import com.ivax.descarregarvideos.ui.home.HomeViewModel
import com.ivax.descarregarvideos.ui.search.SearchFragment

class SavedVideosFragment : Fragment() {
    private var _binding: FragmentSavedVideosBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(        inflater: LayoutInflater,
                                       container: ViewGroup?,
                                       savedInstanceState: Bundle?): View {
        val savedVideosViewModel =
            ViewModelProvider(this, SavedVideosViewModel.Factory).get(SavedVideosViewModel::class.java)

        _binding = FragmentSavedVideosBinding.inflate(inflater, container, false)
        val root: View = binding.root
        binding.recylcerViewSavedVideos.layoutManager =
            LinearLayoutManager(this@SavedVideosFragment.context)
        savedVideosViewModel.allSavedVideos.observe(viewLifecycleOwner) {
            val savedVideoAdapter=SavedVideosAdapter(it)
            binding.recylcerViewSavedVideos.adapter=savedVideoAdapter
            //binding.recylcerViewSavedVideos.
        }


        /*val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}