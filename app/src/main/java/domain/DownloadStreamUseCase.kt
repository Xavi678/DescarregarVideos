package domain

import com.ivax.descarregarvideos.api.YoutubeRepository
import com.ivax.descarregarvideos.responses.PlayerResponse

class DownloadStreamUseCase {
    private val repository= YoutubeRepository()

    suspend operator fun  invoke(url: String): ByteArray? {
        return repository.DownloadVideoStream(url)
    }
}