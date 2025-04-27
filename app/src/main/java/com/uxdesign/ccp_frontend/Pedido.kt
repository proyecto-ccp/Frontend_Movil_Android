package com.uxdesign.ccp_frontend

data class Pedido(
    val idCliente: String,
    val fechaEntrega: String,
    val estadoPedido: String,
    val valorTotal: Double,
    val idVendedor: String,
    val comentarios: String,
    val idMoneda: Int
)
