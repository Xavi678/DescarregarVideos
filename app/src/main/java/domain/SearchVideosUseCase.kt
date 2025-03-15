package domain

import com.ivax.descarregarvideos.api.YoutubeRepository
import com.ivax.descarregarvideos.classes.VideoItem

class SearchVideosUseCase {
    private val repository= YoutubeRepository()

    suspend operator fun  invoke(searchQuery: String): ArrayList<VideoItem> {
        return repository.Search(searchQuery)
    }
}