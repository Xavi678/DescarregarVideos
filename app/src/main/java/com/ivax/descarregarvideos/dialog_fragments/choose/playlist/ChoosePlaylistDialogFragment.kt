package com.ivax.descarregarvideos.dialog_fragments.choose.playlist

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.adapter.PlaylistListAdapter
import com.ivax.descarregarvideos.databinding.DialogChoosePlaylistBinding
import com.ivax.descarregarvideos.dialog_fragments.nova.playlist.NewPlaylistDialogFragment
import com.ivax.descarregarvideos.ui.saved.videos.SavedVideosFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChoosePlaylistDialogFragment : DialogFragment() {

    private var _binding: DialogChoosePlaylistBinding? = null
    private var playlistListAdapter=PlaylistListAdapter()
    private lateinit var videoId : String
    val choosePlaylistViewModel : ChoosePlaylistViewModel by lazy{
        ViewModelProvider(this)[ChoosePlaylistViewModel::class.java]}
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=DialogChoosePlaylistBinding.inflate(inflater,container,false)
        videoId= requireArguments().getString("videoId").toString()
        val root: View =binding.root
        setupUi()
        choosePlaylistViewModel.allPlaylists.observe(this) {
            playlistListAdapter.addItems(it)

        }
        binding.layoutCreatePlaylist.setOnClickListener { view ->
            val newPlaylistDialogFragment=NewPlaylistDialogFragment()
            val bundle=Bundle()
            bundle.putString("videoId",videoId)
            newPlaylistDialogFragment.arguments=bundle
            newPlaylistDialogFragment.show(requireActivity().supportFragmentManager,"DescarregarVideos")
        }
        return root
    }

    fun setupUi(){
        binding.choosePlaylistRecyclerView.layoutManager =
            LinearLayoutManager(this@ChoosePlaylistDialogFragment.context)
        binding.choosePlaylistRecyclerView.adapter=playlistListAdapter
    }
    /*override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater.
            val inflater = requireActivity().layoutInflater;

            // Inflate and set the layout for the dialog.
            // Pass null as the parent view because it's going in the dialog
            // layout.
            builder.setView(inflater.inflate(R.layout.dialog_choose_playlist, null))
                // Add action buttons.
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }*/
}