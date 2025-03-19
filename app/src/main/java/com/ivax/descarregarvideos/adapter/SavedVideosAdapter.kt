package com.ivax.descarregarvideos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.adapter.VideoAdapter.ViewHolder
import com.ivax.descarregarvideos.entities.SavedVideo

class SavedVideosAdapter(val items: List<SavedVideo>) : RecyclerView.Adapter<SavedVideosAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.saved_video_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: SavedVideosAdapter.ViewHolder,
        position: Int
    ) {
            var item=items[position]
            holder.tbxVideoId.text =item.videoId

    }

    override fun getItemCount(): Int {
       return items.size
    }
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tbxVideoId=itemView.findViewById<TextView>(R.id.tbxVideoid)
    }
}