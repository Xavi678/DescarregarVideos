package com.ivax.descarregarvideos.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.media3.common.MediaItem
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.classes.MainDiffCallBack
import com.ivax.descarregarvideos.entities.SavedVideo
import java.io.FileInputStream

class SavedVideosAdapter(private val playMedia: (MediaItem, SavedVideo) -> Unit,private val openMenu: (String)->Unit) : RecyclerView.Adapter<SavedVideosAdapter.ViewHolder>() {

    private val items = ArrayList<SavedVideo>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.saved_video_item, parent, false)
        return ViewHolder(itemView)
    }

    fun addItems(items: List<SavedVideo>) {
        val diffResult = DiffUtil.calculateDiff(MainDiffCallBack(this.items, items))
        this.items.clear()
        this.items.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(
        holder: SavedVideosAdapter.ViewHolder,
        position: Int
    ) {
        var item = items[position]
        var bmp: Bitmap
        var fileInStream= FileInputStream(item.imgUrl)
        fileInStream.use {
            bmp = BitmapFactory.decodeStream(it)
        }
        fileInStream.close()
        holder.imgThumbnail.setImageBitmap(bmp)
        holder.tbxTitle.text=item.title
        holder.buttonPlay.setOnClickListener { view->
            if(item.videoUrl!=null) {
                val mediaItem = MediaItem.fromUri(item.videoUrl!!)
                playMedia(mediaItem,item)
            }

        }
        holder.buttonOptions.setOnClickListener { view->
            openMenu(item.videoId)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgThumbnail: ImageView = itemView.findViewById<ImageView>(R.id.imageViewPlaylist)
        val tbxTitle: TextView =itemView.findViewById<TextView>(R.id.tbxSavedVideoDesc)
        val buttonPlay: ImageButton = itemView.findViewById<ImageButton>(R.id.playButton)
        val buttonOptions: ImageButton=itemView.findViewById<ImageButton>(R.id.savedVideoButtonMenuOptions)
    }
}