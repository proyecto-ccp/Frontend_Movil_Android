package com.uxdesign.ccp_frontend

data class RespuestaVendedor(
    val resultado: Int,
    val mensaje: String,
    val status: Int,
    val vendedor: Vendedor
)
