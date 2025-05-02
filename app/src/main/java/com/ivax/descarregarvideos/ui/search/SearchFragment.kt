package com.ivax.descarregarvideos.ui.search

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.adapter.VideoAdapter
import com.ivax.descarregarvideos.classes.VideoItem
import com.ivax.descarregarvideos.custom.composables.ButtonState
import com.ivax.descarregarvideos.custom.composables.SearchComposable
import com.ivax.descarregarvideos.custom.composables.bounceClick
import com.ivax.descarregarvideos.databinding.FragmentSearchBinding
import com.ivax.descarregarvideos.entities.SavedVideo
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileInputStream

//import org.jsoup.Jsoup

//import org.openqa.selenium.chrome.ChromeDriver

//import org.openqa.selenium.WebDriver
//import org.openqa.selenium.chrome.ChromeDriver

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private var nextToken: String? = null

    private lateinit var adapter: VideoAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val searchViewModel: SearchViewModel by lazy {
        ViewModelProvider(this)[SearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        binding.composeViewSearch.setContent {
            Column {
                SearchComposable(onClickInvoker = fun(text: String) {
                    searchViewModel.SearchVideos(text)
                })
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Loading()
                    SearchVideos()
                }

            }
        }
        adapter =
            VideoAdapter(itemClickListener = fun(saveVideo: SavedVideo, finished: () -> Unit) {
                searchViewModel.downloadVideoResponse(saveVideo, finished)

            })
        val root: View = binding.root
        /*binding.layoutSearchSearch.btnSearch.setOnClickListener { view ->
            var searchQuery = binding.layoutSearchSearch.tbxView.text.toString()

            lifecycleScope.launch {
                searchViewModel.isLoading.collectLatest {

                    binding.searchVideoProgressBar.isVisible = it
                }
            }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    try {

                        searchViewModel.SearchVideos(searchQuery)
                        searchViewModel.searchModel.collectLatest {
                            if(it!=null){
                                adapter.addItems(it.videos)
                                nextToken=it.nextToken

                            }

                        }
                    } catch (e: Exception) {
                        e.message.toString().let {
                            Log.d("DescarregarVideos", it)
                        }
                    }
                }
            }

        }*/
        /*val textView: TextView = binding.textHome
        searchViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }*/
        setupUi()
        return root
    }

    private fun setupUi() {
        /*binding.recylcerViewVideo.layoutManager =
            LinearLayoutManager(this@SearchFragment.context)
        binding.recylcerViewVideo.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            var currentPos=0
            private var loading = true
            private var previousTotalItems = 0
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                currentPos=currentPos+dy
                val linearLayout=recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = linearLayout.childCount
                val totalItemCount = linearLayout.itemCount
                val firstVisibleItemPosition = linearLayout.findFirstVisibleItemPosition()

                if (loading && totalItemCount > previousTotalItems) {
                    loading = false
                    previousTotalItems = totalItemCount
                }

                if (loading && totalItemCount > previousTotalItems) {
                    loading = false
                    previousTotalItems = totalItemCount
                }
                Log.d("DescarregarVideos","Current Pos: ${currentPos}")
                Log.d("DescarregarVideos","${recyclerView.scrollY} ${recyclerView.height} ${recyclerView.y} ${dx} ${dy}")
                val visibleThreshold = 5
                if (!loading && (totalItemCount - visibleItemCount <= firstVisibleItemPosition + visibleThreshold)) {
                    searchViewModel.SearchVideos(binding.layoutSearchSearch.tbxView.text.toString(),nextToken)
                    loading = true
                }
            }
        })
        binding.recylcerViewVideo.adapter = adapter*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    @Composable
    fun SearchVideos(
        searchViewModel: SearchViewModel = viewModel()
    ) {
        val searchResponseFoo by searchViewModel.searchResponseFoo.collectAsStateWithLifecycle()
        if (searchResponseFoo != null) {
            LazyColumn(Modifier.fillMaxSize()) {
                items(searchResponseFoo!!.videos) {
                    Item(it)
                }
            }
        }
    }

    @Composable
    fun Loading(searchViewModel: SearchViewModel = viewModel()) {
        val isLoading by searchViewModel.isLoading.collectAsStateWithLifecycle()
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }

    enum class DownloadState { NotDownloaded, Downloaded, Downloading }

    @Composable
    fun Item(video: VideoItem) {


        Row() {

            Box(
                Modifier
                    .width(86.dp)
                    .padding(top = 8.dp, start = 8.dp)
                    .background(
                        Color.Blue
                    )
            ) {
                Image(
                    bitmap = video.imgUrl!!.asImageBitmap(),
                    contentDescription = null,
                )

                /*Image(
                    painter = painterResource(id = R.drawable.play_button_rect_mod),
                    contentDescription = null,
                    modifier = Modifier
                        .align(alignment = Alignment.Center)
                        //.bounceClick()
                        .clickable(
                            enabled = true,
                            onClick = {
                            },

                            )
                )*/
                Text(
                    text = video.duration,
                    color = Color.White,
                    modifier = Modifier
                        .align(alignment = Alignment.BottomStart)
                        .background(Color.Black)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .weight(1f)
            ) {
                Text(
                    text = video.title,
                    fontSize = 16.sp
                )
                Row {
                    if (video.channelThumbnail != null) {
                        Image(
                            bitmap = video.channelThumbnail!!.asImageBitmap(),
                            contentDescription = "Channel Video Thumbnail"
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Text(
                        text = video.author.toString(),
                        fontSize = 13.sp,
                        modifier = Modifier.align(alignment = Alignment.CenterVertically)
                    )
                }
                Text(
                    text = video.viewCount,
                    fontSize = 11.sp,
                    color = Color.LightGray
                )


            }
            var downloadState by remember { mutableStateOf(DownloadState.NotDownloaded) }
            searchViewModel.hasVideo(video.videoId)
            IconButton(onClick = {
                downloadState = DownloadState.Downloading
                val imgPath = "${video.videoId}_thumbnail.bmp"
                var dir = File("${context?.filesDir}/fotos")
                var d = dir.mkdir()
                var f = File("${dir}/${imgPath}")

                if (f.exists()) {
                    f.delete()
                }
                f.createNewFile()
                f.outputStream().use {
                    video.imgUrl?.compress(Bitmap.CompressFormat.PNG, 100, it)
                }
                var saveVideo = SavedVideo(
                    video.videoId,
                    video.title,
                    "${dir}/${imgPath}",
                    video.duration,
                    video.viewCount,
                    author = video.author
                )
                searchViewModel.downloadVideoResponse(saveVideo, finished = fun() {
                    downloadState= DownloadState.Downloaded
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context,"Video ${saveVideo.videoId} Descarregat Correctament",
                            Toast.LENGTH_LONG).show()
                    }


                })
                /*savedVideosViewModel.setBottomSheetVisibility(true)
                savedVideosViewModel.setBottomSheetVideoId(data.videoId)*/
            }) {
                Icon(
                    painter = painterResource(
                        id = when
                                     (downloadState) {
                            DownloadState.NotDownloaded
                                -> R.drawable.download_button

                            DownloadState.Downloading ->
                                R.drawable.downloading

                            else -> R.drawable.finished_downloading
                        }
                    ),
                    contentDescription = null,

                    modifier = Modifier
                        .animateContentSize()
                        .align(alignment = Alignment.CenterVertically)
                )
            }

        }
        HorizontalDivider(Modifier.padding(4.dp), color = Color.LightGray)
    }
}