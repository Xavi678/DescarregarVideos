package com.ivax.descarregarvideos.ui.theme

import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable

@Composable
fun CustomInputColors() : TextFieldColors{
     return OutlinedTextFieldDefaults.colors(cursorColor = MaterialTheme.colorScheme.surface,
        focusedContainerColor = MaterialTheme.colorScheme.primary,
        unfocusedContainerColor = MaterialTheme.colorScheme.primary,
        unfocusedTextColor = MaterialTheme.colorScheme.surface,
        focusedTextColor = MaterialTheme.colorScheme.surface,
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.surface,
        focusedPlaceholderColor = MaterialTheme.colorScheme.surface,
        disabledBorderColor = MaterialTheme.colorScheme.surface,
        focusedBorderColor = MaterialTheme.colorScheme.surface,
        errorBorderColor = MaterialTheme.colorScheme.surface,
        unfocusedBorderColor = MaterialTheme.colorScheme.surface
        )
}

@Composable
fun CustomCardColors() : CardColors {
   return CardDefaults.cardColors(containerColor =  MaterialTheme.colorScheme.primary,
      disabledContentColor =  MaterialTheme.colorScheme.surface,
      contentColor =  MaterialTheme.colorScheme.surface,
      disabledContainerColor =  MaterialTheme.colorScheme.primary)
}
