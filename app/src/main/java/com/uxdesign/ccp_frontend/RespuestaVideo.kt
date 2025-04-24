package com.uxdesign.ccp_frontend

data class RespuestaVideo(
    val resultado: Int,
    val mensaje: String,
    val status: Int,
    val videos: List<Video>
)
