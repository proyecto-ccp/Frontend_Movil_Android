package com.uxdesign.ccp_frontend

data class RespuestaProducto(
    val resultado: Int,
    val mensaje: String,
    val status: Int,
    val productos: List<Producto>
)
