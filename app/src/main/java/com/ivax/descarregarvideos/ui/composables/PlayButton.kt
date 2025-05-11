package com.ivax.descarregarvideos.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ivax.descarregarvideos.R

@Composable
fun PlayButton(onClickDelegate : () -> Unit ){
    Row {
        Button(modifier = Modifier.height(36.dp),onClick = {
            onClickDelegate()
        }, colors = ButtonColors(
            contentColor = Color.White,
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContentColor = Color.White,
            disabledContainerColor = MaterialTheme.colorScheme.primary)) {
            Text("Reproduir", fontSize = 12.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(painter = painterResource(R.drawable.play_button),
                contentDescription = "Play Icon")
        }
    }
}