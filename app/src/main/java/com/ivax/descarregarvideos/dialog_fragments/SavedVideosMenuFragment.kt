package com.ivax.descarregarvideos.dialog_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.ui.saved.videos.SavedVideosViewModel

class SavedVideosMenuFragment : BottomSheetDialogFragment() {
    val savedVideosMenuViewModel : SavedVideosMenuViewModel by lazy{
        ViewModelProvider(this)[SavedVideosMenuViewModel::class.java]}
    lateinit var videoId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        videoId= requireArguments().getString("id").toString()
        return inflater.inflate(R.layout.fragment_saved_video_menu, container, false)
    }
}