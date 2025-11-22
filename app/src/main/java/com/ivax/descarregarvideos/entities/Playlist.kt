package com.ivax.descarregarvideos.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Ignore
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Playlist ( @PrimaryKey(autoGenerate = true) val playListId: Int=0,@ColumnInfo(name = "name") val name: String?){
    @Ignore var checked: Boolean=false
}