package com.ivax.descarregarvideos.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SavedVideo(@PrimaryKey val videoId: String) {
}