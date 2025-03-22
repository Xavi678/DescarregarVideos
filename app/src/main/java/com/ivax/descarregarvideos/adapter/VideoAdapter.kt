package com.ivax.descarregarvideos.adapter

import android.content.Context
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.classes.MainDiffCallBack
import com.ivax.descarregarvideos.classes.VideoItem
import com.ivax.descarregarvideos.entities.SavedVideo
import java.io.File

class VideoAdapter(private val itemClickListener: (saveVideo: SavedVideo,finished: ()->Unit) -> Unit) :
    RecyclerView.Adapter<VideoAdapter.ViewHolder>() {


    private val items= ArrayList<VideoItem>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.video_item, parent, false)
        return ViewHolder(itemView)
    }

    fun addItems(items: List<VideoItem>) {
        val diffResult = DiffUtil.calculateDiff(MainDiffCallBack(this.items, items))
        this.items.clear()
        this.items.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

            val item = items[position];

            holder.downloadButton.setOnClickListener { view ->

                val imgPath="${item.videoId}_thumbnail.bmp"
                var dir=File("${holder.itemView.context.filesDir}/fotos")
                var d=dir.mkdir()
                var f=File("${dir}/${imgPath}")

                if(f.exists()){
                    f.delete()
                }
                f.createNewFile()
                f.outputStream().use{
                    item.imgUrl?.compress(Bitmap.CompressFormat.PNG,100,it)
                }
                var saveVideo=SavedVideo(item.videoId,item.title,"${dir}/${imgPath}",item.duration,item.viewCount)
                (view as ImageButton).setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.context.resources,R.drawable.downloading,null))
                var callback=fun(){
                    view.setImageDrawable(ResourcesCompat.getDrawable(holder.itemView.context.resources,R.drawable.finished_downloading,null))
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(holder.itemView.context,"${item.title} Descarregat Correctament",
                            Toast.LENGTH_LONG).show()
                    }

                }
                itemClickListener(saveVideo,callback)

                Log.d("DescarregarVideos", "Descarregar Video ${saveVideo.videoId}")
            }

            holder.apply {
                tbx.text = item.videoId
                tbxDesc.text = item.title
                thumbnail.setImageBitmap(item.imgUrl)
                duration.text = item.duration
                viewCount.text = item.viewCount
            }
    }

    override fun getItemCount(): Int {

        return items.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tbx: TextView = itemView.findViewById<TextView>(R.id.tbxVideoId)
        val tbxDesc: TextView = itemView.findViewById<TextView>(R.id.tbxVideoDesc)
        val thumbnail: ImageView = itemView.findViewById<ImageView>(R.id.imgVideoThumbnail)
        val duration: TextView = itemView.findViewById<TextView>(R.id.videoDuration)
        val viewCount: TextView = itemView.findViewById<TextView>(R.id.tbxViewCount)
        val downloadButton: ImageButton = itemView.findViewById<ImageButton>(R.id.downloadButton)

    }
}

