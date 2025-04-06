package com.ivax.descarregarvideos.dialog_fragments.del.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.ivax.descarregarvideos.databinding.DialogDeletePlaylistBinding
import com.ivax.descarregarvideos.dialog_fragments.choose.playlist.ChoosePlaylistViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeletePlaylistDialogFragment : DialogFragment() {

    private var _binding: DialogDeletePlaylistBinding? = null

    private val binding get() = _binding!!

    val choosePlaylistViewModel : DeletePlaylistViewModel by lazy{
        ViewModelProvider(this)[DeletePlaylistViewModel::class.java]}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogDeletePlaylistBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val playlistId=requireArguments().getInt("playlistId")
        binding.btnDeletePlaylistOk.setOnClickListener {
            choosePlaylistViewModel.deletePlaylist(playlistId)
            close()
        }

        binding.btnDeletePlaylistCancel.setOnClickListener {
            close()
        }

        return root
    }

    fun close(){
        dialog?.dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}