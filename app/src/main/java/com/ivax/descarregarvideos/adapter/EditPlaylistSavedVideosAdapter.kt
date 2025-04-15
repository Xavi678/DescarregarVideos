package com.ivax.descarregarvideos.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.adapter.VideoAdapter.ViewHolder
import com.ivax.descarregarvideos.classes.MainDiffCallBack
import com.ivax.descarregarvideos.classes.VideoItem
import com.ivax.descarregarvideos.classes.VideosWithPositionFoo
import com.ivax.descarregarvideos.entities.SavedVideo
import java.io.FileInputStream
import java.util.Collections

class EditPlaylistSavedVideosAdapter :
    RecyclerView.Adapter<EditPlaylistSavedVideosAdapter.ViewHolder>() {
    private val items = ArrayList<VideosWithPositionFoo>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.edit_playlist_item, parent, false)
        return ViewHolder(itemView)
    }

    fun addItems(items: List<VideosWithPositionFoo>) {
        val diffResult = DiffUtil.calculateDiff(MainDiffCallBack(this.items, items))
        this.items.clear()
        this.items.addAll(items)
        this.items.sortBy { it.position }
        diffResult.dispatchUpdatesTo(this)
    }

    @SuppressLint("ClickableViewAccessibility")
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
            gripButton.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(
                    v: View?,
                    event: MotionEvent?
                ): Boolean {
                    if(event?.action==MotionEvent.ACTION_DOWN){
                        callback(holder)
                    }
                    return false
                }

            })
        }
    }
    fun onRowMoved(fromPosition: Int,toPosition : Int): Pair<VideosWithPositionFoo,VideosWithPositionFoo> {
        var from=items[fromPosition]
        var to=items[toPosition]
        Collections.swap(items,fromPosition,toPosition)
        notifyItemMoved(fromPosition, toPosition);
        from.position=toPosition
        to.position=fromPosition

        return Pair<VideosWithPositionFoo, VideosWithPositionFoo>(from,to)
    }

    override fun getItemCount(): Int {
        return items.size
    }
    private lateinit var callback : (EditPlaylistSavedVideosAdapter.ViewHolder) -> Unit
    fun setCallbackDrag(callback: (EditPlaylistSavedVideosAdapter.ViewHolder) -> Unit) {
        this.callback=callback
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val videoDurationEdit = itemView.findViewById<TextView>(R.id.videoDurationEdit)
        val thumbnail=itemView.findViewById<ImageView>(R.id.imageViewEditPlaylist)
        val title=itemView.findViewById<TextView>(R.id.tbxSavedVideoEditDesc)
        val gripButton=itemView.findViewById<ImageView>(R.id.btnGrip)
    }
}