package com.ivax.descarregarvideos.helpers

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject


class CustomFileWriter @Inject constructor(private val appContext: Context) : FileWriter {
    override suspend fun write(fileNme: String, bytes: ByteArray): String {
        try {
            val contentValues = ContentValues()
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "$fileNme.mp4")
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp4")
            contentValues.put(
                MediaStore.MediaColumns.RELATIVE_PATH,
                "${Environment.DIRECTORY_MUSIC}/DescarregarVideos"
            )

            val inserted = appContext.contentResolver.insert(
                MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY),
                contentValues
            )?.also {
                appContext.contentResolver.openOutputStream(it).use {
                        it?.write(bytes)
                }

            }
            return inserted.toString()
        } catch (e: Exception) {
            e.message?.let { Log.d("DescarregarVideos", it) }
            throw e;
        }
    }

}