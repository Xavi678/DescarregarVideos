package com.ivax.descarregarvideos.ui.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ivax.descarregarvideos.R

@Composable
fun SearchComposable(onClickInvoker: (text: String) -> Unit) {
    var text by rememberSaveable { mutableStateOf("") }
    Row(
        modifier = Modifier
            .height(intrinsicSize = IntrinsicSize.Max)
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
            },
            placeholder = {
                Text(text = "Search...")
            },
            leadingIcon = {
                IconButton(onClick = {
                    onClickInvoker(text)
                }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon", tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            trailingIcon = {
                if (text.isNotEmpty()) {
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
            modifier = Modifier
                /*.border(
                    4.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(12.dp, 0.dp, 0.dp, 12.dp)
                )*/
                .weight(1f), shape = RoundedCornerShape(12.dp)
        )
        /*Button(
            onClick = {
                onClickInvoker(text)
            }, shape = RoundedCornerShape(0.dp, 12.dp, 12.dp, 0.dp), colors =
                ButtonColors(
                    Color(29, 27, 32, 255),
                    Color(29, 27, 32, 255),
                    Color.LightGray,
                    Color.LightGray
                ),
            modifier = Modifier.fillMaxHeight()//.weight(0.5f)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_menu_search),
                contentDescription = "Search Icon", tint = Color.White
            )
        }*/
    }
}