package com.ivax.descarregarvideos.ui.edit.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ivax.descarregarvideos.adapter.EditPlaylistSavedVideosAdapter
import com.ivax.descarregarvideos.databinding.FragmentEditPlaylistBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditPlaylistFragment : Fragment() {

    private var _binding : FragmentEditPlaylistBinding?= null
    private val binding get()=_binding!!
    private val editPlaylistViewModel : EditPlaylistViewModel by lazy {
        ViewModelProvider(this)[EditPlaylistViewModel::class.java]
    }
    private val adapter : EditPlaylistSavedVideosAdapter = EditPlaylistSavedVideosAdapter()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding=FragmentEditPlaylistBinding.inflate(inflater,container,false)
        val root: View = binding.root
        val playlistId=requireArguments().getInt("playlistId")
        editPlaylistViewModel.getPlaylist(playlistId)
        lifecycleScope.launch {
            editPlaylistViewModel.playlistIdWithPositions.collectLatest {
                if (it!=null){
                    //binding.tbxEditPlaylistTitle.text=it.
                    adapter.addItems(it)
                }
            }

        }
        binding.playlistControls.shuffle.setOnClickListener {
            editPlaylistViewModel.shuffle(playlistId)
        }
        binding.playlistControls.playAll.setOnClickListener {
            editPlaylistViewModel.playAll(playlistId)
        }
        lifecycleScope.launch {
            editPlaylistViewModel.playlist.collectLatest {
                binding.tbxEditPlaylistTitle.text=it?.name
            }
        }

/*        binding.imageButtonEditPlaylistGoBack.setOnClickListener {
            val navController = requireActivity(). findNavController(R.id.nav_host_fragment_content_main)
            navController.navigate(R.id.nav_playlists)
        }*/
        setupUI()

        return root
    }

    fun setupUI(){
        binding.recylcerViewEDitPlaylistVideos.layoutManager= LinearLayoutManager(this@EditPlaylistFragment.context)
        val itemTouchHelper=ItemTouchHelper(object : ItemTouchHelper.Callback(){
            override fun isLongPressDragEnabled(): Boolean {
                return false
            }
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN;
                return makeMovementFlags(dragFlags,0)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val (from,to)=adapter.onRowMoved(viewHolder.bindingAdapterPosition,target.bindingAdapterPosition)

                editPlaylistViewModel.UpdatePlaylistSavedVideoCrossRef(from)
                editPlaylistViewModel.UpdatePlaylistSavedVideoCrossRef(to)
                return true
            }

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                TODO("Not yet implemented")
            }

        })
        adapter.setCallbackDrag(callback = fun(holder: EditPlaylistSavedVideosAdapter.ViewHolder){
            itemTouchHelper.startDrag(holder)
        })
        itemTouchHelper.attachToRecyclerView(binding.recylcerViewEDitPlaylistVideos)
        binding.recylcerViewEDitPlaylistVideos.adapter=adapter

    }
}