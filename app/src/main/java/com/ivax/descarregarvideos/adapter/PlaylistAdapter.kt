package com.ivax.descarregarvideos.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.classes.MainDiffCallBack
import com.ivax.descarregarvideos.classes.PlaylistWithOrderedVideosFoo
import com.ivax.descarregarvideos.entities.relationships.PlaylistWithSavedVideos
import java.io.FileInputStream

class PlaylistAdapter(private val playAll: (PlaylistWithOrderedVideosFoo) -> Unit,
                      private val shuffle: (PlaylistWithOrderedVideosFoo) -> Unit,
                        private val firstVideo: (Int)-> Unit
) : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {
    private var listener: ((playlistId: Int) -> Unit)? = null

    fun setListener(listener: ((playlistId: Int) -> Unit)?) {
        this.listener = listener
    }
    private val items = ArrayList<PlaylistWithOrderedVideosFoo>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return ViewHolder(itemView)
    }

    fun addItems(items: ArrayList<PlaylistWithOrderedVideosFoo>) {
        val diffResult = DiffUtil.calculateDiff(MainDiffCallBack(this.items, items))
        this.items.clear()
        this.items.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val playlistWSavedVideos: PlaylistWithOrderedVideosFoo = items[position]
        holder.tbxPlaylistTitle.text=playlistWSavedVideos.playlist.name
        firstVideo(playlistWSavedVideos.playlist.playListId)
       val firstVideo= playlistWSavedVideos.orderedVideos.minByOrNull { it.position }
        if(firstVideo?.imgUrl!=null){

            var bmp: Bitmap
            var fileInStream= FileInputStream(firstVideo.imgUrl)
            fileInStream.use {
                bmp = BitmapFactory.decodeStream(it)
            }
            fileInStream.close()
            holder.img.setImageBitmap(bmp)

        }
        holder.playlistCount.text = playlistWSavedVideos.orderedVideos.count().toString()
        holder.playButton.setOnClickListener {
            playAll(playlistWSavedVideos)
        }
        holder.playlistConstraint.setOnClickListener {
            val navController=it.findNavController()
            val bundle=Bundle()
            bundle.putInt("playlistId",playlistWSavedVideos.playlist.playListId)
            navController.navigate(R.id.nav_edit_playlist,bundle)
        }
        holder.shuffleButton.setOnClickListener {
            shuffle(playlistWSavedVideos)
        }
        holder.savedVideoMenu.setOnClickListener {

            listener?.invoke(playlistWSavedVideos.playlist.playListId)


            Log.d("DescarregarVideos","1")
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tbxPlaylistTitle= itemView.findViewById<TextView>(R.id.tbxPlaylistTitle)
        val img=itemView.findViewById<ImageView>(R.id.imgPlaylistFirst)
        val playButton=itemView.findViewById<LinearLayout>(R.id.playAll)
        val shuffleButton=itemView.findViewById<LinearLayout>(R.id.shuffle)
        val playlistCount=itemView.findViewById<TextView>(R.id.tbxPlaylistCountVideos)
        val playlistConstraint=itemView.findViewById<ConstraintLayout>(R.id.layoutPlaylist)
        val savedVideoMenu=itemView.findViewById<ImageButton>(R.id.imageButtonPlaylistOptions)
    }
}