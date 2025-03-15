package domain

import com.ivax.descarregarvideos.api.YoutubeRepository
import com.ivax.descarregarvideos.classes.VideoItem
import com.ivax.descarregarvideos.responses.PlayerResponse

class GetVideoDataUseCase {
    private val repository= YoutubeRepository()

    suspend operator fun  invoke(videoId: String): PlayerResponse {
        return repository.GetVideoData(videoId)
    }
}