package com.ivax.descarregarvideos.dialog_fragments.edit.playlist.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ivax.descarregarvideos.databinding.FragmentEditPlaylistMenuBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditPlaylistMenuFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentEditPlaylistMenuBinding? = null

    private val binding get() = _binding!!
    private val editPlaylistMenuViewModel by lazy {
        ViewModelProvider(this)[EditPlaylistMenuViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditPlaylistMenuBinding.inflate(inflater, container, false)
        val playlistId=requireArguments().getInt("playlistId")
        val root: View = binding.root
        binding.layoutEditPlaylistMenuDelete.setOnClickListener {
            editPlaylistMenuViewModel.deletePlaylist(playlistId)
            dismiss()
        }
        return root
    }
}