package com.uxdesign.ccp_frontend

data class RespuestaPedido(
    val pedidos: List<Pedido>,
    val resultado: Int,
    val mensaje: String,
    val status: Int,
)
