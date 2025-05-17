package com.ivax.descarregarvideos

import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.media3.common.MediaItem
import com.ivax.descarregarvideos.databinding.ActivityMainBinding
import com.ivax.descarregarvideos.general.viewmodels.MediaViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.FileInputStream
import android.widget.LinearLayout
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.session.MediaController
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.common.base.Objects
import com.google.common.util.concurrent.ListenableFuture
import com.ivax.descarregarvideos.classes.RouteLabel
import com.ivax.descarregarvideos.ui.composables.AddPlaylistMenu
import com.ivax.descarregarvideos.ui.edit.playlist.EditPlaylistScreen
import com.ivax.descarregarvideos.ui.edit.playlist.EditPlaylistViewModel
import com.ivax.descarregarvideos.ui.routes.Route
import com.ivax.descarregarvideos.ui.routes.Route.Playlists
import com.ivax.descarregarvideos.ui.playlists.PlaylistScreen
import com.ivax.descarregarvideos.ui.playlists.PlaylistsViewModel
import com.ivax.descarregarvideos.ui.routes.Route.EditPlaylist.*
import com.ivax.descarregarvideos.ui.saved.videos.SavedVideosViewModel
import com.ivax.descarregarvideos.ui.saved.videos.SearchAudioScreen
import com.ivax.descarregarvideos.ui.search.SearchScreen
import com.ivax.descarregarvideos.ui.search.SearchViewModel
import com.ivax.descarregarvideos.ui.theme.MainAppTheme
import kotlinx.coroutines.flow.update




@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var player: MediaController
    private lateinit var btnSkipBackward: ImageButton
    private lateinit var btnSkipForward: ImageButton
    private lateinit var btnMinimizePlayer: ImageButton
    private lateinit var btnDestroyPlayer: ImageButton
    private lateinit var controllerFuture: ListenableFuture<MediaController>
    private var playlistLayout: LinearLayout? = null
    private lateinit var tbxPlaylistName: TextView


    private val mediaViewModel: MediaViewModel by lazy {
        ViewModelProvider(this).get(MediaViewModel::class.java)
    }

    override fun onResume() {

        super.onResume()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }

    override fun onStart() {
        /*binding.appBarMain.composeViewMain.setContent {
            MainWrapper()

        }
        val sessionToken =
            SessionToken(this, ComponentName(this, PlaybackService::class.java))
        controllerFuture =
            MediaController.Builder(this, sessionToken).buildAsync()
        controllerFuture.addListener({
            try {
                val mediaController = controllerFuture.get()
                mediaViewModel.setMediaController(mediaController)
                player = mediaViewModel.getMediaPlayer()
                binding.appBarMain.playerView.player = player
                player.repeatMode = Player.REPEAT_MODE_ALL
                if (player.isPlaying) {
                    val mediaItem = player.currentMediaItem
                    if (mediaItem != null) {
                        mediaViewModel.isMediaPlayerMaximized.postValue(true)
                        setMetadata(mediaItem)
                    }

                }
                //val defaultTimeBar=binding.appBarMain.root.findViewById<DefaultTimeBar>(R.id.defaultTimeBar)
                //defaultTimeBar.
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

                    override fun onPlaybackStateChanged(playbackState: Int) {
                        Log.d("DescarregarVideos", "Playback Changed")
                        super.onPlaybackStateChanged(playbackState)
                    }

                    override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                        Log.d("DescarregarVideos", "Timeline Chnaged")
                        super.onTimelineChanged(timeline, reason)
                    }

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        hasNextAndPreviousMedia()
                        super.onIsPlayingChanged(isPlaying)
                    }

                    override fun onMediaItemTransition(
                        mediaItem: MediaItem?,
                        @MediaItemTransitionReason reason: Int
                    ) {
                        if (mediaItem != null) {
                            setMetadata(mediaItem)

                        }
                    }


                })
            } catch (e: Exception) {
                Log.d("DescarregarVideos", e.message.toString())
            }
            // MediaController is available here with controllerFuture.get()
        }, MoreExecutors.directExecutor())

*/
        super.onStart()
    }

    private fun setMetadata(mediaItem: MediaItem) {
        hasNextAndPreviousMedia()
        val title = mediaItem.mediaMetadata.title
        val uri = mediaItem.mediaMetadata.artworkUri
        val playlistName = mediaItem.mediaMetadata.albumTitle
        var bmp: Bitmap
        var fileInStream = FileInputStream(uri.toString())
        fileInStream.use {
            bmp = BitmapFactory.decodeStream(it)
        }
        fileInStream.close()
        mediaViewModel.playlistName.update {
            playlistName?.toString()
        }
        mediaViewModel.thumbnail.update {
            bmp
        }
        mediaViewModel.title.update {
            title.toString()
        }
    }

    override fun onStop() {

        MediaController.releaseFuture(controllerFuture)
        super.onStop()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            MainAppTheme {
                MainWrapper()

            }
        }
        //binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)


        /*var playButton =
            binding.appBarMain.root.findViewById<ImageButton>(R.id.mediaPlayerPlayButton)
        var thumbnailPlayer =
            binding.appBarMain.root.findViewById<ImageView>(R.id.mediaPlayerThumbnail)
        var playerSongTextView =
            binding.appBarMain.root.findViewById<TextView>(R.id.playerSongTextView)
        btnSkipBackward = binding.appBarMain.root.findViewById<ImageButton>(R.id.skipBackward)
        btnSkipForward = binding.appBarMain.root.findViewById<ImageButton>(R.id.skipBForward)
        btnMinimizePlayer =
            binding.appBarMain.root.findViewById<ImageButton>(R.id.imageButtonMinimizePlayer)
        btnDestroyPlayer =
            binding.appBarMain.root.findViewById<ImageButton>(R.id.imageButtonDestroyPlayer)
        tbxPlaylistName = binding.appBarMain.root.findViewById<TextView>(R.id.tbxCurrentPlaylist)
        playlistLayout =
            binding.appBarMain.root.findViewById<LinearLayout>(R.id.layoutCurrentPlaylist)
        mediaViewModel.isMediaPlayerMaximized.observe(this) {

            binding.appBarMain.mediaPlayer.visibility = if (it) View.VISIBLE else View.GONE
            binding.appBarMain.fab.visibility = if (it) View.GONE else View.VISIBLE
        }
        lifecycleScope.launch {
            mediaViewModel.isMediaPlayerVisible.collectLatest {
                if (!it) {
                    binding.appBarMain.fab.visibility = View.GONE
                    binding.appBarMain.mediaPlayer.visibility = View.GONE
                }
            }
        }

        btnSkipBackward.setOnClickListener {
            player.seekToPreviousMediaItem()
        }

        btnSkipForward.setOnClickListener {
            player.seekToNextMediaItem()
        }

        lifecycleScope.launch {
            mediaViewModel.title.collectLatest {
                Log.d(
                    "DescarregarVideos",
                    "Visibilitat Player: ${binding.appBarMain.playerView.visibility}"
                )

                playerSongTextView.text = it
                playerSongTextView.isSelected = true
            }
        }
        lifecycleScope.launch {
            mediaViewModel.thumbnail.collectLatest {
                if (it != null) {
                    thumbnailPlayer.setImageBitmap(it)
                }
            }
        }
        lifecycleScope.launch {
            mediaViewModel.playlistName.collectLatest {

                if (playlistLayout != null) {
                    if (it != null) {
                        playlistLayout!!.visibility = View.VISIBLE
                        tbxPlaylistName.text = it
                    } else {

                        playlistLayout!!.visibility = View.INVISIBLE
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
            mediaViewModel.isMediaPlayerMaximized.postValue(false)
        }
        btnDestroyPlayer.setOnClickListener {
            player.stop()
            player.clearMediaItems()
            player.release()
            mediaViewModel.isMediaPlayerVisible.update {
                false
            }

        }



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

            mediaViewModel.isMediaPlayerMaximized.postValue(true)
        }

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_search,
                R.id.nav_saved_videos, R.id.nav_playlists
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)*/
    }

    private fun hasNextAndPreviousMedia() {
        mediaViewModel.playlistHasPrevious.update {
            player.hasPreviousMediaItem()
        }
        mediaViewModel.playlistHasNext.update {
            player.hasNextMediaItem()
        }
    }

    private fun hasNextMedia(it: Boolean) {
        btnSkipForward.visibility = if (it) View.VISIBLE
        else View.INVISIBLE
    }

    private fun hasPreviousMedia(it: Boolean) {
        btnSkipBackward.visibility = if (it) View.VISIBLE
        else View.INVISIBLE
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

    @Composable
    fun HomeNavHost(
        modifier: Modifier,
        navController: NavHostController,
        startDestination: Any,
        navigateTo: (route: String) -> Unit
    ) {
        NavHost(
            navController = navController, startDestination = startDestination, modifier = modifier
        ) {

            composable<Route.Search> {
                val viewModel = hiltViewModel<SearchViewModel>()
                SearchScreen(viewModel)
            }
            composable<Playlists> {
                val viewModel = hiltViewModel<PlaylistsViewModel>()
                PlaylistScreen(viewModel, fun(playlistId: Int) {
                    navController.navigate(Route.EditPlaylist(playlistId)) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                })
            }
            composable<Route.SavedAudio> {
                val viewModel = hiltViewModel<SavedVideosViewModel>()
                SearchAudioScreen(viewModel)
            }

            composable<Route.EditPlaylist> {
                val arg = it.toRoute<Route.EditPlaylist>()
                val viewModel = hiltViewModel<EditPlaylistViewModel>()
                EditPlaylistScreen(arg.playlistId, viewModel)
            }

        }
    }

    @Composable
    fun MainWrapper() {
        val navController = rememberNavController()
        MainScaffold {
            navController.navigate(it)
        }
        AddPlaylistMenu()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainScaffold(
        navigateTo: (route: String) -> Unit
    ) {
        val navController = rememberNavController()


        val currentBackState by navController.currentBackStackEntryAsState()
        val ruta=currentBackState.getRoute()


        Scaffold(
            containerColor = Color.White, topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(text = ruta.toString())
                    }
                )
            },
            bottomBar = {
                NavBar(navController)
            }) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                HomeNavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f),
                    navController = navController,
                    startDestination = Route.Search,
                    navigateTo = navigateTo
                )

            }
        }
    }

    @Composable
    fun NavBar(navController: NavController) {
       val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val navItems= listOf(
            Route.Search,
            Route.SavedAudio,
            Route.Playlists
        )

        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(),
            contentColor = MaterialTheme.colorScheme.primary,
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ) {
            navItems.forEach {
                NavigationBarItem(
                    selected = currentBackStackEntry?.destination?.hasRoute(it::class)==true, onClick = {
                        navController.navigate(it) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }, icon = {
                        Icon(
                            painter = painterResource(it.icon!!),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(it.label)
                    },
                    colors = NavigationBarItemColors(
                        selectedIndicatorColor = MaterialTheme.colorScheme.inversePrimary,
                        disabledIconColor = Color.LightGray,
                        disabledTextColor = Color.LightGray,
                        selectedIconColor = MaterialTheme.colorScheme.primary,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedIconColor = Color.Black,
                        unselectedTextColor = Color.Black
                    )
                )
            }

            /*NavigationBarItem(
                selected = false, onClick = {
                    navController.navigate(Route.SavedAudio) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }, icon = {
                    Icon(
                        painter = painterResource(R.drawable.download_rounded_base),
                        contentDescription = null,
                    )
                },
                label = {
                    Text("Saved Audio")
                }
            )
            NavigationBarItem(
                selected = false, onClick = {
                    navController.navigate(Route.Playlists) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                }, icon = {
                    Icon(
                        painter = painterResource(R.drawable.collection_fill),
                        contentDescription = null,
                    )
                },
                label = {
                    Text("Playlists")
                }
            )*/


        }
    }
}

private fun NavBackStackEntry?.getRoute() : String? {
    return this?.let {
        when{
            destination.hasRoute<Route.Search>()->Route.Search.label
            destination.hasRoute<Route.SavedAudio>()->Route.SavedAudio.label
            destination.hasRoute<Route.Playlists>()->Route.Playlists.label
            destination.hasRoute<Route.EditPlaylist>()->Route.EditPlaylist.Companion.get().label
            else -> null
        }
    }
}
