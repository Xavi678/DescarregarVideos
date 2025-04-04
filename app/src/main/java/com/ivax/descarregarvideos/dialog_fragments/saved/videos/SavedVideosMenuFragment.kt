package com.ivax.descarregarvideos.dialog_fragments.saved.videos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.databinding.FragmentSavedVideoMenuBinding
import com.ivax.descarregarvideos.dialog_fragments.choose.playlist.ChoosePlaylistDialogFragment

class SavedVideosMenuFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentSavedVideoMenuBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
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
        _binding=FragmentSavedVideoMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //_binding=inflater.inflate()
        videoId= requireArguments().getString("id").toString()
        binding.layoutAddToPlaylist.setOnClickListener { view ->

            OpenChoosePlaylistDialog()
        }
        return root
    }

    private fun OpenChoosePlaylistDialog() {
        var choosePlaylistDialogFragment = ChoosePlaylistDialogFragment()
        val bundle=Bundle()
        bundle.putString("videoId",videoId)
        choosePlaylistDialogFragment.arguments=bundle
        choosePlaylistDialogFragment.show(
            requireActivity().supportFragmentManager,
            "DescarregarVideos"
        )
    }
}