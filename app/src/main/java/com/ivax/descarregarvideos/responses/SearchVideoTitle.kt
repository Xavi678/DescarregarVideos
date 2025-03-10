package com.ivax.descarregarvideos.responses

import kotlinx.serialization.Serializable

@Serializable
data class SearchVideoTitle(val runs: List<SearchVideoTitleRun>) {
}