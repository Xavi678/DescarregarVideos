package com.ivax.descarregarvideos.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.classes.VideoItem

class VideoAdapter(val items: List<VideoItem>?) : RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        if(items!=null) {
            val item = items[position];

            /*holder.downloadButton.setOnClickListener { view->

            searchViewModel.downloadVideo(item.videoId)
        }*/
            holder.apply {
                tbx.text = item.videoId
                tbxDesc.text = item.title
                thumbnail.setImageBitmap(item.imgUrl)
                duration.text = item.duration
                viewCount.text = item.viewCount
            }

        }
    }

    override fun getItemCount(): Int {

        return if(items?.size==null) 0 else items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tbx: TextView = itemView.findViewById<TextView>(R.id.tbxVideoId)
        val tbxDesc: TextView = itemView.findViewById<TextView>(R.id.tbxVideoDesc)
        val thumbnail: ImageView = itemView.findViewById<ImageView>(R.id.imgVideoThumbnail)
        val duration: TextView = itemView.findViewById<TextView>(R.id.videoDuration)
        val viewCount: TextView = itemView.findViewById<TextView>(R.id.tbxViewCount)
        val downloadButton: ImageButton=itemView.findViewById<ImageButton>(R.id.downloadButton)

    }
}