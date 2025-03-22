package com.ivax.descarregarvideos.ui.saved.videos

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.ivax.descarregarvideos.adapter.SavedVideosAdapter
import com.ivax.descarregarvideos.databinding.FragmentSavedVideosBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SavedVideosFragment : Fragment() {
    private var _binding: FragmentSavedVideosBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var savedVideoAdapter: SavedVideosAdapter
    val savedVideosViewModel : SavedVideosViewModel by lazy{
        ViewModelProvider(this)[SavedVideosViewModel::class.java]}

    override fun onCreateView(        inflater: LayoutInflater,
                                       container: ViewGroup?,
                                       savedInstanceState: Bundle?): View {

        _binding = FragmentSavedVideosBinding.inflate(inflater, container, false)
        val root: View = binding.root
        savedVideoAdapter= SavedVideosAdapter(playMedia= fun(mediaItem: MediaItem){
            savedVideosViewModel.addItemMedia(mediaItem)
            savedVideosViewModel.play()
        })
        savedVideosViewModel.allSavedVideos.observe(viewLifecycleOwner) {
            savedVideoAdapter.addItems(it)
        }
        setupUI()
        return root
    }
    fun setupUI(){
        binding.recylcerViewSavedVideos.layoutManager =
            LinearLayoutManager(this@SavedVideosFragment.context)
        binding.recylcerViewSavedVideos.adapter=savedVideoAdapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}