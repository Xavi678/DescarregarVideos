package com.ivax.descarregarvideos.helpers

import android.content.Context
import java.io.File
import java.util.Date
import javax.inject.Inject

class CustomFileWriter @Inject constructor(private val appContext: Context): FileWriter {
    override suspend fun write(fileNme: String,bytes: ByteArray) : String {
        val dir=File("${appContext.filesDir}/videos")
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
        return videoUrl
        /*var out=appContext.openFileOutput(
            "",
            Context.MODE_PRIVATE
        )
            .use {


            }
*/
    }

}