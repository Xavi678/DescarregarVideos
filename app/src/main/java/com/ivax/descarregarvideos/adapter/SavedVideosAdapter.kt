package com.ivax.descarregarvideos.adapter

import android.content.Context
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
import com.ivax.descarregarvideos.entities.SavedVideo

class SavedVideosAdapter() : RecyclerView.Adapter<SavedVideosAdapter.ViewHolder>() {

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
        holder.itemView.context.openFileInput(item.imgUrl).use {
            bmp = BitmapFactory.decodeStream(it)
        }
        holder.imgThumbnail.setImageBitmap(bmp)
        holder.tbxTitle.text=item.title
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgThumbnail = itemView.findViewById<ImageView>(R.id.imgSavedVideoThumbnail)
        val tbxTitle=itemView.findViewById<TextView>(R.id.tbxSavedVideoDesc)
    }
}