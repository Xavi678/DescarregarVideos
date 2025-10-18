package com.ivax.descarregarvideos.helpers

import android.R.attr.mimeType
import android.content.ContentValues
import android.content.Context
import android.icu.lang.UScript.getShortName
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject


class CustomFileWriter @Inject constructor(private val appContext: Context) : FileWriter {
    override suspend fun write(fileNme: String, bytes: ByteArray): String {

        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, fileNme)
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "mp4")
        contentValues.put(
            MediaStore.MediaColumns.RELATIVE_PATH,
            "${Environment.DIRECTORY_MUSIC}/DescarregarVideos"
        )

        val inserted = appContext.contentResolver.insert(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        val fullPath = inserted.toString()
        val file = File(fullPath)
        file.outputStream().use {
            it.write(bytes)
        }
        return fullPath
        /*val dir=File("${appContext.filesDir}/videos")
        dir.mkdir()
        var videoUrl="${dir}/${fileNme}.mp4"
        var file=File(videoUrl)
        file.mkdir()
        if(file.exists()){
            file.delete()
        }
        file.createNewFile()
        file.outputStream().use {
            it.write(bytes)
        }
        return videoUrl*/
    }

}