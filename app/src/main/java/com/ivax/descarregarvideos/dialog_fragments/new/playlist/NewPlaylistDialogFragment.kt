package com.ivax.descarregarvideos.dialog_fragments.new.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ivax.descarregarvideos.databinding.DialogNewPlaylistBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewPlaylistDialogFragment : DialogFragment() {

    private var _binding : DialogNewPlaylistBinding?= null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=DialogNewPlaylistBinding.inflate(layoutInflater,container,false)

        return binding.root
    }
}