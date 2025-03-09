package com.ivax.descarregarvideos.requests

import kotlinx.serialization.Serializable

@Serializable
data class PlayerClient(val clientName: String="IOS",val clientVersion:String="19.45.4",
                        val deviceMake:String="Apple",val deviceModel:String="iPhone16,2",
                        val platform:String="MOBILE",
                        val osName:String="IOS",val osVersion:String="18.1.0.22B83",
                        val visitorData:String="Cgs1Z3lEbWtIV2ZkNCjsubO-BjIiCgJFUxIcEhgSFhMLFBUWFwwYGRobHB0eHw4PIBAREiEgFQ%3D%3D",
                        val hl:String="en",val gl: String="US",
    val utcOffsetMinutes:String="0") {
}