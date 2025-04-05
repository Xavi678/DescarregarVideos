package com.ivax.descarregarvideos.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Playlist ( @PrimaryKey(autoGenerate = true) val playListId: Int=0,@ColumnInfo(name = "name") val name: String?)