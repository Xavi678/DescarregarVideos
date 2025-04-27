package com.ivax.descarregarvideos.ui.saved.videos

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.colorspace.Rgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.recyclerview.widget.LinearLayoutManager
import com.ivax.descarregarvideos.R
import com.ivax.descarregarvideos.adapter.SavedVideosAdapter
import com.ivax.descarregarvideos.databinding.FragmentSavedVideosBinding
import com.ivax.descarregarvideos.dialog_fragments.saved.videos.SavedVideosMenuFragment
import com.ivax.descarregarvideos.entities.SavedVideo
import dagger.hilt.android.AndroidEntryPoint
import java.io.FileInputStream

@AndroidEntryPoint
class SavedVideosFragment : Fragment() {
    private var _binding: FragmentSavedVideosBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var savedVideoAdapter: SavedVideosAdapter
    val savedVideosViewModel : SavedVideosViewModel by lazy{
        ViewModelProvider(this)[SavedVideosViewModel::class.java]}

    override fun onCreateView(        inflater: LayoutInflater,
                                       container: ViewGroup?,
                                       savedInstanceState: Bundle?): View {

        _binding = FragmentSavedVideosBinding.inflate(inflater, container, false)
        val root: View = binding.root
        savedVideoAdapter= SavedVideosAdapter(playMedia= fun(savedVideo: SavedVideo){
            savedVideosViewModel.addSingleItemMedia(savedVideo)
            savedVideosViewModel.setSavedVideo(savedVideo)
            savedVideosViewModel.play()
            savedVideosViewModel.setMediaVisibility(true)
        }, openMenu = fun(videoId: String){
            var savedVideosMenuFragment=SavedVideosMenuFragment()
            val bundle = Bundle()
            bundle.putString("id", videoId)
            savedVideosMenuFragment.arguments=bundle
            savedVideosMenuFragment.show(requireActivity().supportFragmentManager,"SavedVideosMenu")

        })
        binding.composeViewPlaylists.setContent {
            Column {
                SearchContent()
                AllVideos()
            }
            ShowBottomDialog()
        }
        /*binding.layoutSavedVideoSearch.btnSearch.setOnClickListener {
            savedVideoAdapter.addItems( savedVideosViewModel.filterSavedVideos(binding.layoutSavedVideoSearch.tbxView.text.toString()))
        }*/
        setupUI()
        return root
    }
    fun setupUI(){
        //binding.recylcerViewSavedVideos.layoutManager =
            LinearLayoutManager(this@SavedVideosFragment.context)
        //binding.recylcerViewSavedVideos.adapter=savedVideoAdapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @Composable
    fun AllVideos(
        savedVideosViewModel : SavedVideosViewModel = viewModel()
    ){
        val savedVideos by savedVideosViewModel.allSavedVideos.collectAsStateWithLifecycle(listOf<SavedVideo>())
            LazyColumn(Modifier.fillMaxSize()) {
                items(savedVideos) {
                    ListItem(it)
                }
        }
    }
    @Preview
    @Composable
    fun SearchContent(savedVideosViewModel : SavedVideosViewModel = viewModel()){
        var text by remember  { mutableStateOf("") }
        Row(modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max).padding(8.dp)) {
                OutlinedTextField(
                    value = text,
                    onValueChange = {
                        text = it
                    },
                    placeholder = {
                        Text(text = "Search...")
                    },
                    trailingIcon = {
                        if (text.count() > 0) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear Icon",
                                Modifier.clickable(
                                    enabled = true,
                                    onClick = {
                                        text = ""
                                    },
                                    indication = ripple(),
                                    interactionSource = remember { MutableInteractionSource() })
                            )
                        }
                    },
                    modifier = Modifier.border(
                        4.dp,
                        Color(29, 27, 32, 255),
                        shape = RoundedCornerShape(12.dp, 0.dp, 0.dp, 12.dp)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                Button(
                    onClick = {
                        savedVideosViewModel.filterSavedVideos(text)
                    }, shape = RoundedCornerShape(0.dp, 12.dp, 12.dp, 0.dp), colors =
                        ButtonColors(
                            Color(29, 27, 32, 255),
                            Color(29, 27, 32, 255),
                            Color.LightGray,
                            Color.LightGray
                        ),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu_search),
                        contentDescription = "Search Icon", tint = Color.White
                    )
                }
        }
    }
    @Composable
    fun ListItem(data: SavedVideo, modifier: Modifier = Modifier,savedVideosViewModel : SavedVideosViewModel = viewModel()) {

        var bmp : Bitmap
        var fileInStream= FileInputStream(data.imgUrl)
        fileInStream.use {
            bmp = BitmapFactory.decodeStream(it)
        }

        fileInStream.close()
        Row(modifier) {
            Box (Modifier.width(86.dp).padding(top = 8.dp, start = 8.dp).background(
                Color.Blue)){
                Image(
                    bitmap = bmp.asImageBitmap(),
                    contentDescription = null,
                )
                Image(painter=painterResource(id=R.drawable.play_button_rect_mod), contentDescription = null,
                    modifier = Modifier.align(alignment = Alignment.Center).clickable(enabled = true, onClick = {
                        savedVideosViewModel.addSingleItemMedia(data)
                        savedVideosViewModel.setSavedVideo(data)
                        savedVideosViewModel.play()
                        savedVideosViewModel.setMediaVisibility(true)
                    }, indication = ripple(color = Color.Magenta), interactionSource = remember { MutableInteractionSource() }))
                Text(text = data.duration, color = Color.White, modifier = Modifier.
                align(alignment = Alignment.BottomStart)
                    .background(Color.Black))
            }


            Text(text = data.title, modifier = Modifier.fillMaxSize().padding(8.dp).weight(1f))
            Image(painter = painterResource(id=R.drawable.three_dots), contentDescription = null,
                modifier= Modifier.align(alignment = Alignment.CenterVertically).clickable(enabled = true, onClick = {
                    savedVideosViewModel.setBottomSheetVisibility(true)
                    savedVideosViewModel.setBottomSheetVideoId(data.videoId)
                }, indication = ripple(), interactionSource = remember { MutableInteractionSource()}))

        }
        HorizontalDivider(Modifier.padding(4.dp), color = Color.LightGray)
    }
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ShowBottomDialog(savedVideosViewModel : SavedVideosViewModel = viewModel()){

        if(savedVideosViewModel.isBottomSheetVisible.collectAsStateWithLifecycle().value){
            val  videoId=savedVideosViewModel.bottomSheetParameter.collectAsStateWithLifecycle().value!!
            ModalBottomSheet(onDismissRequest = {
                savedVideosViewModel.setBottomSheetVisibility(false)
            }, containerColor = Color(29,27,32,255)
            ) {
                Row(modifier = Modifier.fillMaxWidth().padding(8.dp).clickable(enabled = true, onClick = {
                    savedVideosViewModel.deleteVideo(videoId)
                    savedVideosViewModel.setBottomSheetVisibility(false)
                }, indication = ripple(color = Color.Magenta), interactionSource = remember { MutableInteractionSource() }
                ).align(alignment = Alignment.CenterHorizontally)) {

                        Image(
                            painter = painterResource(id = R.drawable.remove_trash),
                            contentDescription = null
                        )

                    Text(text = "Delete Audio", color = Color.White,
                        modifier = Modifier.align(alignment = Alignment.CenterVertically))
                }

            }
        }

    }
}