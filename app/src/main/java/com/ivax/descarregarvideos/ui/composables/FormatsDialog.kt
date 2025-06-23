package com.ivax.descarregarvideos.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ivax.descarregarvideos.responses.AdaptiveFormats
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun FormatsDialog(formats: List<AdaptiveFormats>,onClose: (format: AdaptiveFormats?)->Unit) {
    var selectedFormat by remember {  mutableStateOf<AdaptiveFormats?>(formats.first()) }
    Dialog(onDismissRequest = {

    }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ){
            Column(modifier = Modifier.fillMaxWidth()) {


                LazyColumn {
                    itemsIndexed(formats){
                        idx,item ->
                        AdaptiveFormatItem(item,idx,selectedFormat,fun (format: AdaptiveFormats?){
                            selectedFormat=format
                        })
                    }
                }
                HorizontalDivider()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp)
                        .wrapContentWidth(align = Alignment.End)
                ) {
                    TextButton(onClick = {
                        onClose(null)
                    }, modifier = Modifier) {
                        Text("Cancel·lar", color = MaterialTheme.colorScheme.surface)
                    }
                    TextButton(onClick = {
                        onClose(selectedFormat)

                    }, modifier = Modifier) {
                        Text("Ok", color = MaterialTheme.colorScheme.surface)
                    }
                }
            }
        }
    }
}
@Preview
@Composable
fun FormatsDialogPreview(){
    val formats = ArrayList<AdaptiveFormats>()
    val format=AdaptiveFormats("https://rr1---sn-8vq54voxn25po-cjoz.googlevideo.com/videoplayback?expire=1749687704&ei=OMlJaLiKO7Wip-oP3KbpoAM&ip=46.25.75.44&id=o-AA3FDb6nJnHEOgSGeRV55AB0q4CpX04mp5I7hoBB276W&itag=139&source=youtube&requiressl=yes&xpc=EgVo2aDSNQ%3D%3D&met=1749666104%2C&mh=kK&mm=31%2C29&mn=sn-8vq54voxn25po-cjoz%2Csn-h5qzen7d&ms=au%2Crdu&mv=m&mvi=1&pl=24&rms=au%2Cau&initcwndbps=3101250&bui=AY1jyLNqp5KdZxpe9fO1fvBLNU-6dNK4p74BnWiJtCYrRX99aWyzIS9Tv_Y5wR7qSiGHdznMAPBGV7OS&spc=l3OVKSV7ONTc&vprv=1&svpuc=1&mime=audio%2Fmp4&rqh=1&gir=yes&clen=1092901&dur=179.118&lmt=1705718201358706&mt=1749665355&fvip=5&keepalive=yes&c=IOS&txp=4532434&sparams=expire%2Cei%2Cip%2Cid%2Citag%2Csource%2Crequiressl%2Cxpc%2Cbui%2Cspc%2Cvprv%2Csvpuc%2Cmime%2Crqh%2Cgir%2Cclen%2Cdur%2Clmt&sig=AJfQdSswRQIhALVW3wl8j-AfNP7RPP0TlQt3Ma_aX7ucSTzwh7efwn9xAiAzKvtu4C2guf5IUQ7zBJtD1pUXhHGxDLJ8S8vO7vixVw%3D%3D&lsparams=met%2Cmh%2Cmm%2Cmn%2Cms%2Cmv%2Cmvi%2Cpl%2Crms%2Cinitcwndbps&lsig=APaTxxMwRQIgXRmIc-ogMKE2iZB1lqulXX14o9PuT8sbCnS44EVnbCICIQC7F7Vl0VDuQ-bnJYBVtVZ2XDKvCVH6JzPVrMUyLmG-RA%3D%3D",
        mimeType = "audio/mp4; codecs=\"mp4a.40.5\"", projectionType = "RECTANGULAR", quality = "")
    formats.add(format)
    FormatsDialog(formats){

    }
}
@Composable
fun AdaptiveFormatItem(adaptiveFormat: AdaptiveFormats,idx: Int,selectedFormat: AdaptiveFormats?,onSelected: (url: AdaptiveFormats?)->Unit){
    Row(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Text(adaptiveFormat.mimeType, modifier = Modifier.weight(1f).align(alignment = Alignment.CenterVertically))
        RadioButton(selected = if(selectedFormat==null) idx==1 else selectedFormat.url==adaptiveFormat.url, onClick = {
            onSelected(adaptiveFormat)
        } , modifier = Modifier.align(alignment = Alignment.CenterVertically).padding(end = 0.dp).wrapContentWidth(align = Alignment.End),
            colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.surface,
                unselectedColor = MaterialTheme.colorScheme.surface)
            )
    }

}