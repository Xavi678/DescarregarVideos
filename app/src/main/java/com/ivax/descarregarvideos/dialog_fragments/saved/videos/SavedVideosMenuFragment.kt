package com.ivax.descarregarvideos.dialog_fragments.saved.videos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.databinding.FragmentSavedVideoMenuBinding
import com.ivax.descarregarvideos.dialog_fragments.choose.playlist.ChoosePlaylistDialogFragment
import com.ivax.descarregarvideos.dialog_fragments.del.video.DeleteVideoDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        binding.layoutDeleteAudio.setOnClickListener { view ->
            OpenDeleteVideoDialog()
        }
        return root
    }

    private fun OpenDeleteVideoDialog() {
       val deleteVideoDialogFragment= DeleteVideoDialogFragment()
        openDialog(deleteVideoDialogFragment)
    }
    private fun openDialog(dialogFragment: DialogFragment){
        val bundle= Bundle()
        bundle.putString("videoId",videoId)
        dialogFragment.arguments=bundle
        dialogFragment.show(
            requireActivity().supportFragmentManager,
            "DescarregarVideos"
        )
    }

    private fun OpenChoosePlaylistDialog() {
        val choosePlaylistDialogFragment = ChoosePlaylistDialogFragment()
        openDialog(choosePlaylistDialogFragment)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}