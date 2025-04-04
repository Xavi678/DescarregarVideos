package com.ivax.descarregarvideos

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.ivax.descarregarvideos.classes.CustomTimer
import com.ivax.descarregarvideos.classes.MathExtensions
import com.ivax.descarregarvideos.databinding.ActivityMainBinding
import com.ivax.descarregarvideos.general.viewmodels.MediaViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.FileInputStream
import android.view.animation.AnimationUtils


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var tbxTimeDuration: TextView
    private lateinit var seekBarI: SeekBar
    //private var timer=Timer()
    private var seekBarProgress=0
    private var isTimerCancelled=false
    private var isSeeking=false

    val callbackTimer=fun (){
        if (!isSeeking) {
            /*if (isTimerCancelled) {
                this.cancel()
            }*/
            seekBarProgress += 1
            Handler(Looper.getMainLooper()).post {
                tbxTimeDuration.text =
                    MathExtensions.toTime(seekBarProgress)
                seekBarI.progress = seekBarProgress
            }
        }
    }
    private var timer=CustomTimer(callbackTimer)

    private val mediaViewModel : MediaViewModel by lazy {
        ViewModelProvider(this).get(MediaViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        var player=mediaViewModel.getMediaPlayer()

        binding.appBarMain.playerView.player=player
        var playButton=binding.appBarMain.root.findViewById<ImageButton>(R.id.mediaPlayerPlayButton)
        var thumbnailPlayer=binding.appBarMain.root.findViewById<ImageView>(R.id.mediaPlayerThumbnail)
        var playerSongTextView=binding.appBarMain.root.findViewById<TextView>(R.id.playerSongTextView)
        tbxTimeDuration=binding.appBarMain.root.findViewById<TextView>(R.id.tbxTimeDuration)
        seekBarI=binding.appBarMain.root.findViewById<SeekBar>(R.id.seekBar)
        val animMove=AnimationUtils.loadAnimation(this,R.anim.move)
        var tbxTimeTotal=binding.appBarMain.root.findViewById<TextView>(R.id.tbxTimeTotal)

        mediaViewModel.currentMedia.observe(this) {
            it.videoUrl
            var bmp: Bitmap
            var fileInStream= FileInputStream(it.imgUrl)
            fileInStream.use {
                bmp = BitmapFactory.decodeStream(it)
            }
            fileInStream.close()
            thumbnailPlayer.setImageBitmap(bmp)
            playerSongTextView.text=it.title
            //playerSongTextView.startAnimation(animMove)
            //thumbnailPlayer.setImageBitmap(it)
        }


        seekBarI.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if(fromUser){
                    try {
                        timer.cancel()
                    }catch (e: Exception){
                        Log.d("DescarregarVideos",e.message.toString())
                    }
                    var seekTo=(progress*1000).toLong()
                    Log.d("DescarregarVideos",seekTo.toString())
                    seekBarProgress=progress
                    player.seekTo(seekTo)
                    tbxTimeDuration.text = MathExtensions.toTime(progress)
                    playButton.setImageDrawable(ResourcesCompat.getDrawable(this@MainActivity.resources,R.drawable.pause_button_white,null) )
                    try {



                            timer.schedule()

                    }catch (e: Exception){
                        Log.d("DescarregarVideos",e.message.toString())
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isSeeking=true
                isTimerCancelled=true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isSeeking=false
                isTimerCancelled=false
                player.prepare()
                player.play()

            }

        })
        player.addListener( object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                tbxTimeTotal.text= MathExtensions.toTime ((player.duration/1000).toInt())
                if(!isSeeking) {

                    var total = player.duration / 1000
                    seekBarI.min = 0
                    seekBarI.max = total.toInt()
                    var position = player.currentPosition / 1000
                    Log.d("DescarregarVideos",(player.currentPosition / 1000).toString())
                    seekBarProgress = position.toInt()
                    seekBarI.progress = seekBarProgress

                    tbxTimeDuration.text = MathExtensions.toTime(seekBarProgress)
                    //val fixedRateTimer = fixedRateTimer(
                    if (isPlaying) {
                        isTimerCancelled = false
                        try {
                            //timerTask= TimerTask()
                            timer.schedule()
                        }catch (e: Exception){
                            e.message.let {  Log.d("DescarregarVideo", it.toString())}

                        }

                        // Active playback.
                    } else {
                        try {
                            //timer.cancel()
                            timer.cancel()
                        }catch (e: Exception){
                            Log.d("DescarregarVideos",e.message.toString())
                        }
                        isTimerCancelled = true
                        // Not playing because playback is paused, ended, suppressed, or the player
                        // is buffering, stopped or failed. Check player.playWhenReady,
                        // player.playbackState, player.playbackSuppressionReason and
                        // player.playerError for details.
                    }
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                Log.d("DescarregarVideos","")
                super.onMediaItemTransition(mediaItem, reason)
                Log.d("DescarregarVideos","${mediaItem?.mediaMetadata}")

            }


        })
        playButton.setOnClickListener { view->
            var playPause=(view as ImageButton)
            if(player.isPlaying){
                //playPause.context.resources
                player.stop()
                playPause.setImageDrawable(ResourcesCompat.getDrawable(view.context.resources,R.drawable.play_button_round,null) )
            }else{
                player.prepare()
                player.play()
                playPause.setImageDrawable(ResourcesCompat.getDrawable(view.context.resources,R.drawable.pause_button_white,null) )
            }
            Log.d("DescarregarVideos","")
        }

        /*binding.appBarMain.mediaPlayerPlayButton.setOnClickListener {

        }*/
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_search,
                R.id.nav_saved_videos
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }




    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
