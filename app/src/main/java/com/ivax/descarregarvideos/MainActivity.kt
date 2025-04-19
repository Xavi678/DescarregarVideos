package com.ivax.descarregarvideos

import android.content.ComponentName
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.View
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
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.Player.MediaItemTransitionReason
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.ivax.descarregarvideos.services.PlaybackService
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var tbxTimeDuration: TextView
    private var seekBarI: SeekBar? =null
    private lateinit var player: MediaController
    private lateinit var btnSkipBackward: ImageButton
    private lateinit var btnSkipForward: ImageButton
    private lateinit var btnMinimizePlayer: ImageButton
    private lateinit var controllerFuture : ListenableFuture<MediaController>
    private  var playlistLayout: LinearLayout?=null
    private lateinit var tbxPlaylistName: TextView
    //private var timer=Timer()
    private var seekBarProgress = 0
    private var isTimerCancelled = false
    private var isSeeking = false
    private lateinit var tbxTimeTotal: TextView

    val callbackTimer = fun() {
        if (!isSeeking) {
            /*if (isTimerCancelled) {
                this.cancel()
            }*/
            seekBarProgress += 1
            Handler(Looper.getMainLooper()).post {
                if (seekBarProgress <= (player.duration / 1000F).toInt()) {
                    tbxTimeDuration.text =
                        MathExtensions.toTime(seekBarProgress)
                }

                seekBarI!!.progress = seekBarProgress
            }
        }
    }
    private var timer = CustomTimer(callbackTimer)

    private val mediaViewModel: MediaViewModel by lazy {
        ViewModelProvider(this).get(MediaViewModel::class.java)
    }

    override fun onResume() {
        if(seekBarI!=null) {
            try {
                seekBarI?.progress = (player.currentPosition / 1000F).toInt()
            } catch (e: Exception) {
                Log.d("DescarregarVideos", e.message.toString())
            }
        }
        super.onResume()
    }
    override fun onStart() {
        val sessionToken =
            SessionToken(this, ComponentName(this, PlaybackService::class.java))
        controllerFuture =
            MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener({
            try{
                val mediaController=controllerFuture.get()
                mediaViewModel.setMediaController(mediaController)
                player = mediaViewModel.getMediaPlayer()
                binding.appBarMain.playerView.player = player
                player.addListener(object : Player.Listener {
                    override fun onPositionDiscontinuity(
                        oldPosition: Player.PositionInfo,
                        newPosition: Player.PositionInfo,
                        reason: Int
                    ) {
                        Log.d("DescarregarVideos", "Old: ${oldPosition.positionMs}")
                        Log.d("DescarregarVideos", "New: ${newPosition.positionMs}")
                        super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                    }

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        hasNextAndPreviousMedia()
                        if (!isSeeking) {
                            setTotalDuration()

                            var position = player.currentPosition / 1000f
                            Log.d("DescarregarVideos", (player.currentPosition / 1000f).toString())
                            seekBarProgress = position.toInt()
                            seekBarI!!.progress = seekBarProgress

                            tbxTimeDuration.text = MathExtensions.toTime(seekBarProgress)
                            //val fixedRateTimer = fixedRateTimer(
                            if (isPlaying) {
                                isTimerCancelled = false
                                try {
                                    //timerTask= TimerTask()
                                    timer.schedule()
                                } catch (e: Exception) {
                                    e.message.let { Log.d("DescarregarVideo", it.toString()) }

                                }

                                // Active playback.
                            } else {
                                try {
                                    //timer.cancel()
                                    timer.cancel()
                                } catch (e: Exception) {
                                    Log.d("DescarregarVideos", e.message.toString())
                                }
                                isTimerCancelled = true
                                // Not playing because playback is paused, ended, suppressed, or the player
                                // is buffering, stopped or failed. Check player.playWhenReady,
                                // player.playbackState, player.playbackSuppressionReason and
                                // player.playerError for details.
                            }
                        }
                    }

                    override fun onMediaItemTransition(
                        mediaItem: MediaItem?,
                        @MediaItemTransitionReason reason: Int
                    ) {
                        hasNextAndPreviousMedia()
                        seekBarProgress = 0
                        if (player.duration != Long.MIN_VALUE) {
                            setTotalDuration()
                        }
                        val title = mediaItem?.mediaMetadata?.title
                        val uri = mediaItem?.mediaMetadata?.artworkUri
                       val playlistName= mediaItem?.mediaMetadata?.albumTitle
                        var bmp: Bitmap
                        var fileInStream = FileInputStream(uri.toString())
                        fileInStream.use {
                            bmp = BitmapFactory.decodeStream(it)
                        }
                        fileInStream.close()
                        mediaViewModel.playlistName.update{
                            playlistName?.toString()
                        }
                        mediaViewModel.thumbnail.update {
                            bmp
                        }
                        mediaViewModel.title.update {
                            title.toString()
                        }
                        //playerSongTextView.text =
                        Log.d("DescarregarVideos", "")

                        //super.onMediaItemTransition(mediaItem, reason)
                        Log.d("DescarregarVideos", "${mediaItem?.mediaMetadata}")

                    }


                })
        }catch (e: Exception){
            Log.d("DescarregarVideos",e.message.toString())
        }
            // MediaController is available here with controllerFuture.get()
        }, MoreExecutors.directExecutor())


        super.onStart()
    }

    override fun onStop() {

        MediaController.releaseFuture(controllerFuture)
        super.onStop()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)



        var playButton =
            binding.appBarMain.root.findViewById<ImageButton>(R.id.mediaPlayerPlayButton)
        var thumbnailPlayer =
            binding.appBarMain.root.findViewById<ImageView>(R.id.mediaPlayerThumbnail)
        var playerSongTextView =
            binding.appBarMain.root.findViewById<TextView>(R.id.playerSongTextView)
        btnSkipBackward = binding.appBarMain.root.findViewById<ImageButton>(R.id.skipBackward)
        btnSkipForward = binding.appBarMain.root.findViewById<ImageButton>(R.id.skipBForward)
        tbxTimeDuration = binding.appBarMain.root.findViewById<TextView>(R.id.tbxTimeDuration)
        seekBarI = binding.appBarMain.root.findViewById<SeekBar>(R.id.seekBar)
        tbxTimeTotal = binding.appBarMain.root.findViewById<TextView>(R.id.tbxTimeTotal)
        btnMinimizePlayer=binding.appBarMain.root.findViewById<ImageButton>(R.id.imageButtonMinimizePlayer)
        tbxPlaylistName=binding.appBarMain.root.findViewById<TextView>(R.id.tbxPlayListName)
        playlistLayout=binding.appBarMain.root.findViewById<LinearLayout>(R.id.layoutPlaylist)
        mediaViewModel.isMediaVisible.observe(this) {

            binding.appBarMain.mediaPlayer.visibility = if (it) View.VISIBLE else View.GONE
            binding.appBarMain.fab.visibility= if(it) View.GONE else View.VISIBLE
        }
        btnSkipBackward.setOnClickListener {
            player.seekToPreviousMediaItem()
        }

        btnSkipForward.setOnClickListener {
            player.seekToNextMediaItem()
        }
        //binding.appBarMain.mediaPlayer.visibility=View.VISIBLE
        /*mediaViewModel.currentMedia.observe(this) {
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
        }*/
        lifecycleScope.launch {
            mediaViewModel.title.collectLatest {
                Log.d("DescarregarVideos","Visibilitat Player: ${binding.appBarMain.playerView.visibility}")

                playerSongTextView.text = it
                playerSongTextView.isSelected=true
            }
        }
        lifecycleScope.launch {
            mediaViewModel.thumbnail.collectLatest {
                if(it!=null) {
                    thumbnailPlayer.setImageBitmap(it)
                }
            }
        }
        lifecycleScope.launch {
            mediaViewModel.playlistName.collectLatest {

                if(playlistLayout!=null) {
                    if (it != null) {
                        playlistLayout!!.visibility = View.VISIBLE
                        tbxPlaylistName.text = it
                    } else {

                        playlistLayout!!.visibility = View.GONE
                    }
                }
            }
        }
        lifecycleScope.launch {
            mediaViewModel.playlistHasPrevious.collectLatest {
                hasPreviousMedia(it)
            }
        }

        lifecycleScope.launch {
            mediaViewModel.playlistHasNext.collectLatest {
                hasNextMedia(it)
            }
        }
        btnMinimizePlayer.setOnClickListener {
            mediaViewModel.isMediaVisible.postValue(false)
        }
        seekBarI!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                if (fromUser) {
                    try {
                        timer.cancel()
                    } catch (e: Exception) {
                        Log.d("DescarregarVideos", e.message.toString())
                    }
                    var seekTo = (progress * 1000).toLong()
                    Log.d("DescarregarVideos", seekTo.toString())
                    seekBarProgress = progress
                    player.seekTo(seekTo)
                    tbxTimeDuration.text = MathExtensions.toTime(progress)
                    playButton.setImageDrawable(
                        ResourcesCompat.getDrawable(
                            this@MainActivity.resources,
                            R.drawable.pause_button_white,
                            null
                        )
                    )
                    try {


                        timer.schedule()

                    } catch (e: Exception) {
                        Log.d("DescarregarVideos", e.message.toString())
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                isSeeking = true
                isTimerCancelled = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                isSeeking = false
                isTimerCancelled = false
                player.prepare()
                player.play()

            }

        })

        playButton.setOnClickListener { view ->
            var playPause = (view as ImageButton)
            if (player.isPlaying) {
                //playPause.context.resources
                player.stop()
                playPause.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        view.context.resources,
                        R.drawable.play_button_round,
                        null
                    )
                )
            } else {
                player.prepare()
                player.play()
                playPause.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        view.context.resources,
                        R.drawable.pause_button_white,
                        null
                    )
                )
            }
            Log.d("DescarregarVideos", "")
        }

        binding.appBarMain.fab.setOnClickListener {

            mediaViewModel.isMediaVisible.postValue(true)
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_search,
                R.id.nav_saved_videos, R.id.nav_playlists
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun hasNextAndPreviousMedia() {
        mediaViewModel.playlistHasPrevious.update {
            player.hasPreviousMediaItem()
        }
        mediaViewModel.playlistHasNext.update {
            player.hasNextMediaItem()
        }
    }

    private fun hasNextMedia(it : Boolean) {
        btnSkipForward.visibility = if (it) View.VISIBLE
        else View.INVISIBLE
    }

    private fun hasPreviousMedia(it : Boolean) {
        btnSkipBackward.visibility = if (it) View.VISIBLE
        else View.INVISIBLE
    }

    fun setTotalDuration() {
        var total = player.duration / 1000f
        seekBarI!!.min = 0
        seekBarI!!.max = total.toInt()
        tbxTimeTotal.text = MathExtensions.toTime((player.duration / 1000F).toInt())
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
