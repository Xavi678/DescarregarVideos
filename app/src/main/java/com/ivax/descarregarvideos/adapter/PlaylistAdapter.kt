package com.ivax.descarregarvideos.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.adapter.ChoosePlaylistListAdapter.ViewHolder
import com.ivax.descarregarvideos.classes.MainDiffCallBack
import com.ivax.descarregarvideos.entities.relationships.PlaylistWithSavedVideos
import java.io.FileInputStream

class PlaylistAdapter(private val playAll: (Int) -> Unit) : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    private val items = ArrayList<PlaylistWithSavedVideos>()
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

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val playlistWSavedVideos: PlaylistWithSavedVideos = items[position]
        holder.tbxPlaylistTitle.text=playlistWSavedVideos.playlist.name

       val firstVideo= playlistWSavedVideos.videos.firstOrNull()
        if(firstVideo?.imgUrl!=null){

            var bmp: Bitmap
            var fileInStream= FileInputStream(firstVideo.imgUrl)
            fileInStream.use {
                bmp = BitmapFactory.decodeStream(it)
            }
            fileInStream.close()
            holder.img.setImageBitmap(bmp)

        }
        holder.playButton.setOnClickListener {
            playAll(playlistWSavedVideos.playlist.playListId)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tbxPlaylistTitle= itemView.findViewById<TextView>(R.id.tbxPlaylistTitle)
        val img=itemView.findViewById<ImageView>(R.id.imgPlaylistFirst)
        val playButton=itemView.findViewById<LinearLayout>(R.id.layoutImageButtonPlaylistPlayAll)
    }
}