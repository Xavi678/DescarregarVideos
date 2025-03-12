package com.ivax.descarregarvideos.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.classes.VideoItem
import kotlinx.coroutines.coroutineScope
import java.net.URL

class VideoAdapter(val items: List<VideoItem>): RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.video_item,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val item=items[position];

            val newurl = URL(item.imgUrl);
            val mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            holder.apply {
                tbx.text = item.videoId
                tbxDesc.text = item.title
                thumbnail.setImageBitmap(mIcon_val)
            }


    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tbx : TextView = itemView.findViewById<TextView>(R.id.tbxVideoId)
        val tbxDesc : TextView = itemView.findViewById<TextView>(R.id.tbxVideoDesc)
        val thumbnail : ImageView = itemView.findViewById<ImageView>(R.id.imgVideoThumbnail)
    }
}