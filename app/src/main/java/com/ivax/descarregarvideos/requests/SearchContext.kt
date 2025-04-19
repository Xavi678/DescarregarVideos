package com.ivax.descarregarvideos.requests

import kotlinx.serialization.Serializable

@Serializable
data class SearchContext(val client: SearchClient= SearchClient(), var continuation: String?=null){}