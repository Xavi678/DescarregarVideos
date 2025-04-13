package com.ivax.descarregarvideos.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.ivax.descarregarvideos.classes.VideoItem
import com.ivax.descarregarvideos.entities.SavedVideo
import java.io.FileInputStream

class EditPlaylistSavedVideosAdapter :
    RecyclerView.Adapter<EditPlaylistSavedVideosAdapter.ViewHolder>() {
    private val items = ArrayList<SavedVideo>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.edit_playlist_item, parent, false)
        return ViewHolder(itemView)
    }

    fun addItems(items: List<SavedVideo>) {
        val diffResult = DiffUtil.calculateDiff(MainDiffCallBack(this.items, items))
        this.items.clear()
        this.items.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val currentItem = items[position]

        if(currentItem.imgUrl!=null){

            var bmp: Bitmap
            var fileInStream= FileInputStream(currentItem.imgUrl)
            fileInStream.use {
                bmp = BitmapFactory.decodeStream(it)
            }
            fileInStream.close()
            holder.thumbnail.setImageBitmap(bmp)

        }

        holder.apply {
            videoDurationEdit.text=currentItem.duration
            title.text=currentItem.title
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoDurationEdit = itemView.findViewById<TextView>(R.id.videoDurationEdit)
        val thumbnail=itemView.findViewById<ImageView>(R.id.imageViewEditPlaylist)
        val title=itemView.findViewById<TextView>(R.id.tbxSavedVideoEditDesc)
    }
}