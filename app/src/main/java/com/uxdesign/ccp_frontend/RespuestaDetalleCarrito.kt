package com.uxdesign.ccp_frontend

data class RespuestaDetalleCarrito(
    val resultado: Int,
    val mensaje: String,
    val status: Int,
    val detallePedidos: List<ProductoCarrito>
)
