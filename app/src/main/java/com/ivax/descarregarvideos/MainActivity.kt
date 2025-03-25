package com.ivax.descarregarvideos

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.SeekBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.ivax.descarregarvideos.databinding.ActivityMainBinding
import com.ivax.descarregarvideos.general.viewmodels.MediaViewModel
import com.ivax.descarregarvideos.requests.PlayerRequest
import com.ivax.descarregarvideos.responses.PlayerResponse
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.launch
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.schedule
import kotlin.concurrent.timer

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
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
        var seekBar=binding.appBarMain.root.findViewById<SeekBar>(R.id.seekBar)

        mediaViewModel.currentThumbnail.observe(this) {
            thumbnailPlayer.setImageBitmap(it)
        }
        var videoWatchedTime = 0L

        Handler(Looper.getMainLooper()).postDelayed({
            videoWatchedTime+=player.currentPosition/1000
        }, 3000)

        var timer=Timer()
        var seekBarProgress=0
        var timerStarted=false

        player.addListener( object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if(timerStarted) {
                    timer.cancel()
                }
                var total=player.duration/1000
                seekBar.min=0
                seekBar.max= total.toInt()
                var position=player.currentPosition/1000
                seekBarProgress=position.toInt()
                seekBar.progress=seekBarProgress
                timer.schedule(object : TimerTask() {
                    override fun run() {
                        seekBarProgress+=1
                        seekBar.progress=seekBarProgress
                    }
                },0,1000)
                timerStarted=true
                //val fixedRateTimer = fixedRateTimer(
                if (isPlaying) {

                    // Active playback.
                } else {
                    // Not playing because playback is paused, ended, suppressed, or the player
                    // is buffering, stopped or failed. Check player.playWhenReady,
                    // player.playbackState, player.playbackSuppressionReason and
                    // player.playerError for details.
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
