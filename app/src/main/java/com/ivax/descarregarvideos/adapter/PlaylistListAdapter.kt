package com.ivax.descarregarvideos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.adapter.VideoAdapter.ViewHolder
import com.ivax.descarregarvideos.classes.MainDiffCallBack
import com.ivax.descarregarvideos.entities.Playlist

class PlaylistListAdapter : RecyclerView.Adapter<PlaylistListAdapter.ViewHolder>() {
    private val items = ArrayList<Playlist>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return ViewHolder(itemView)
    }
    fun addItems(items: List<Playlist>){
        val diffResult = DiffUtil.calculateDiff(MainDiffCallBack(this.items, items))
        this.items.clear()
        this.items.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }
    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
       val item= items[position]

        holder.tbxPlaylistTitle.text=item.name
    }

    override fun getItemCount(): Int {
       return items.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       val tbxPlaylistTitle= itemView.findViewById<TextView>(R.id.tbxPlaylist)
    }
}