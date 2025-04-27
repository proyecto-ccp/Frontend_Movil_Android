package com.uxdesign.ccp_frontend

data class PedidoProcesado(
    val id: String,
    val idCliente: String,
    val fechaRealizado: String,
    val fechaEntrega: String,
    val estadoPedido: String,
    val valorTotal: Double,
    val idVendedor: String,
    val comentarios: String,
    val idMoneda: Int
)
