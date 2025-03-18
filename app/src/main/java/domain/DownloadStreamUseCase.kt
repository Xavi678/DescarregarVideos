package domain

import com.ivax.descarregarvideos.api.YoutubeRepository
import java.io.File
import java.io.FileOutputStream

class DownloadStreamUseCase {
    private val repository= YoutubeRepository()

    suspend operator fun  invoke(url: String): ByteArray {
      return  repository.DownloadVideoStream(url)
    }
}