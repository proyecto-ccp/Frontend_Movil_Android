package com.uxdesign.ccp_frontend

data class RespuestaRequest(
    val id: String,
    val resultado: Int,
    val mensaje: String,
    val status: Int
)
