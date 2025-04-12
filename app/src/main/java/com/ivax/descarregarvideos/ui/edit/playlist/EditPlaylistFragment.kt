package com.ivax.descarregarvideos.ui.edit.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.databinding.FragmentEditPlaylistBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditPlaylistFragment : Fragment() {

    private var _binding : FragmentEditPlaylistBinding?= null
    private val binding get()=_binding!!
    private val editPlaylistViewModel : EditPlaylistViewModel by lazy {
        ViewModelProvider(this)[EditPlaylistViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentEditPlaylistBinding.inflate(inflater,container,false)
        val root: View = binding.root
        val playlistId=requireArguments().getInt("playlistId")
        var playlistWithSavedVideos=editPlaylistViewModel.getPlaylist(playlistId)
        lifecycleScope.launch {
            editPlaylistViewModel.playlistWithSavedVideos.collectLatest {
                if (it!=null){
                    binding.tbxEditPlaylistTitle.text=it.playlist.name
                }
            }
        }

/*        binding.imageButtonEditPlaylistGoBack.setOnClickListener {
            val navController = requireActivity(). findNavController(R.id.nav_host_fragment_content_main)
            navController.navigate(R.id.nav_playlists)
        }*/
        return root
    }
}