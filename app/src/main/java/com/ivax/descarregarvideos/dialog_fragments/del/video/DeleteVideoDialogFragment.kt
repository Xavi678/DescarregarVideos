package com.ivax.descarregarvideos.dialog_fragments.del.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.ivax.descarregarvideos.databinding.DialogDeleteVideoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteVideoDialogFragment : DialogFragment() {
    private var _binding : DialogDeleteVideoBinding?=null

    private val binding get()=_binding!!
    private val viewModel by lazy {
        ViewModelProvider(this)[DeleteVideoViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=DialogDeleteVideoBinding.inflate(inflater,container,false)
        val root: View = binding.root
        val videoId=requireArguments().getString("videoId")!!
        binding.btnDeleteVideoOk.setOnClickListener {
            viewModel.deleteVideo(videoId)
            dismiss()
        }
        binding.btnDeleteVideoCancel.setOnClickListener {
            dismiss()
        }
        return root
    }
}