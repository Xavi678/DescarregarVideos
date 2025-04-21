package com.ivax.descarregarvideos.ui.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivax.descarregarvideos.adapter.PlaylistAdapter
import com.ivax.descarregarvideos.databinding.FragmentHomeBinding
import com.ivax.descarregarvideos.databinding.FragmentPlaylistsBinding
import com.ivax.descarregarvideos.dialog_fragments.edit.playlist.menu.EditPlaylistMenuFragment
import com.ivax.descarregarvideos.entities.relationships.PlaylistWithSavedVideos
import com.ivax.descarregarvideos.ui.search.SearchFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaylistsFragment : Fragment(){
    private var _binding: FragmentPlaylistsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val playlistsViewModel : PlaylistsViewModel by lazy {
        ViewModelProvider(this)[PlaylistsViewModel::class.java]
    }
    private val playlistAdapter: PlaylistAdapter= PlaylistAdapter(playAll = fun (playlist : PlaylistWithSavedVideos){
        playlistsViewModel.addPlaylist(playlist)
        playlistsViewModel.setMediaVisibility(true)
    })
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        playlistsViewModel.playlists.observe(viewLifecycleOwner) {
            playlistAdapter.addItems(it)
        }
        binding.layoutPlaylistsSearch.btnSearch.setOnClickListener {
            val text=binding.layoutPlaylistsSearch.tbxView.text.toString()

            playlistAdapter.addItems(playlistsViewModel.filterPlaylist(text))

        }
        playlistAdapter.setListener { playlistId ->

            val editPlaylistMenuFragment=EditPlaylistMenuFragment()
            var bundle= Bundle()
            bundle.putInt("playlistId",playlistId)
            editPlaylistMenuFragment.arguments=bundle
            editPlaylistMenuFragment.show(requireActivity().supportFragmentManager,"DescarregarVideos")
        }
        setupUI()
        return root
    }

    fun setupUI(){
        binding.recyclerViewPlaylists.layoutManager= LinearLayoutManager(this@PlaylistsFragment.context)
        binding.recyclerViewPlaylists.adapter=playlistAdapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}