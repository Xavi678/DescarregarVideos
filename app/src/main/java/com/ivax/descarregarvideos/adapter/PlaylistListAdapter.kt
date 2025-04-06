package com.ivax.descarregarvideos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.adapter.VideoAdapter.ViewHolder
import com.ivax.descarregarvideos.classes.MainDiffCallBack
import com.ivax.descarregarvideos.dialog_fragments.del.playlist.DeletePlaylistDialogFragment
import com.ivax.descarregarvideos.entities.Playlist
import com.ivax.descarregarvideos.entities.relationships.PlaylistWithSavedVideos

class PlaylistListAdapter(private val openDialog: (Int)->Unit) : RecyclerView.Adapter<PlaylistListAdapter.ViewHolder>() {
    private val items = ArrayList<PlaylistWithSavedVideos>()
    private lateinit var videoId: String
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return ViewHolder(itemView)
    }

    fun addItems(items: List<PlaylistWithSavedVideos>) {
        val diffResult = DiffUtil.calculateDiff(MainDiffCallBack(this.items, items))
        this.items.clear()
        this.items.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    fun getCheckedItems() : List<Playlist>{
        return items.filter { it.playlist.checked }.map { it.playlist }
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item = items[position]
        holder.cbxPlaylist.isChecked=item.videos.firstOrNull { it.videoId==this.videoId }!=null

        holder.tbxPlaylistTitle.text = item.playlist.name
        holder.cbxPlaylist.setOnClickListener { view ->
            var cbx = view as CheckBox
            item.playlist.checked = cbx.isChecked
        }
        holder.btnRemovePlayList.setOnClickListener { view->
            this.openDialog(item.playlist.playListId)
            val deletePlaylistDialogFragment=DeletePlaylistDialogFragment()
            //deletePlaylistDialogFragment.show(holder.itemView)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setVideoId(videoId: String) {
        this.videoId=videoId
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tbxPlaylistTitle = itemView.findViewById<TextView>(R.id.tbxPlaylist)
        val cbxPlaylist = itemView.findViewById<CheckBox>(R.id.checkboxPlaylist)
        val btnRemovePlayList=itemView.findViewById<Button>(R.id.imageButtonRemovePlaylist)
    }
}