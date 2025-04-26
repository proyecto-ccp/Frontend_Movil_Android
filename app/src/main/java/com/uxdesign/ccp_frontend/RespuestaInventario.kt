package com.uxdesign.ccp_frontend

data class RespuestaInventario(
    val resultado: Int,
    val mensaje: String,
    val status: Int,
    val inventario: Inventario
)
