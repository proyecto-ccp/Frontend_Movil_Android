package com.uxdesign.ccp_frontend

data class RespuestaLogin(
    val resultado: Int,
    val mensaje: String,
    val status: Int,
    val token: String,
    val menu: String,
    val idusuario: String
)
