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
import androidx.compose.material3.RadioButton
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

@Composable
fun FormatsDialog(formats: List<AdaptiveFormats>,onClose: (format: AdaptiveFormats?)->Unit) {
    var selectedFormat by remember {  mutableStateOf<AdaptiveFormats?>(null) }
    Dialog(onDismissRequest = {

    }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ){
            Column(modifier = Modifier.fillMaxSize()) {


                LazyColumn {
                    itemsIndexed(formats){
                        idx,item ->
                        AdaptiveFormatItem(item,idx,selectedFormat,fun (format: AdaptiveFormats?){
                            selectedFormat=format
                        })
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp)
                        .wrapContentWidth(align = Alignment.End)
                ) {
                    TextButton(onClick = {
                        onClose(null)
                    }, modifier = Modifier) {
                        Text("Cancel·lar")
                    }
                    TextButton(onClick = {
                        onClose(selectedFormat)

                    }, modifier = Modifier) {
                        Text("Ok")
                    }
                }
            }
        }
    }
}
@Composable
fun AdaptiveFormatItem(adaptiveFormat: AdaptiveFormats,idx: Int,selectedFormat: AdaptiveFormats?,onSelected: (url: AdaptiveFormats?)->Unit){
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(adaptiveFormat.mimeType, modifier = Modifier.padding(start = 8.dp).weight(1f)
            .wrapContentWidth(align = Alignment.Start))
        RadioButton(selected = if(selectedFormat==null) idx==1 else selectedFormat.url==adaptiveFormat.url, onClick = {
            onSelected(adaptiveFormat)
        } , modifier = Modifier.padding(start = 8.dp).weight(1f)
            .wrapContentWidth(align = Alignment.Start))
    }

}