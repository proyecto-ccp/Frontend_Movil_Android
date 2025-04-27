package com.uxdesign.ccp_frontend

data class RespuestaPedidoProcesado(
    val pedidos: List<PedidoProcesado>,
    val resultado: Int,
    val mensaje: String,
    val status: Int,
)
