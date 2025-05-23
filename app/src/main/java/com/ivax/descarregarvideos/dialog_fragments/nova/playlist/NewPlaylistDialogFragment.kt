package com.ivax.descarregarvideos.dialog_fragments.nova.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import com.ivax.descarregarvideos.databinding.DialogNewPlaylistBinding
import com.ivax.descarregarvideos.entities.Playlist
import com.ivax.descarregarvideos.entities.PlaylistSavedVideoCrossRef
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewPlaylistDialogFragment : DialogFragment() {

    private var _binding: DialogNewPlaylistBinding? = null
    val newPlaylistViewModel: NewPlaylistViewModel by lazy {
        ViewModelProvider(this)[NewPlaylistViewModel::class.java]
    }
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogNewPlaylistBinding.inflate(layoutInflater, container, false)
        val videoId = requireArguments().getString("videoId").toString()
        binding.btnNewPlaylistOk.setOnClickListener { view ->
            var playlist = Playlist(name = binding.tbxPlayListName.text.toString())
            newPlaylistViewModel.insertPlaylist(playlist)
            val result = "some string"
            setFragmentResult("requestKey", bundleOf("resultKey" to result))
            dismiss()
            /*newPlaylistViewModel.insertedId.observe(this) {
                if (it != null) {
                    var playlistCrossRef=PlaylistSavedVideoCrossRef(playListId = it, videoId = videoId)
                    if(!newPlaylistViewModel.exists(playlistCrossRef)) {
                        newPlaylistViewModel.insertPlaylistSavedVideo(playlistCrossRef)
                    }
                }
            }*/
            close()
        }
        binding.btnNewPlaylistCancel.setOnClickListener {
            close()
        }
        return binding.root
    }

    fun close() {
        dialog?.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}