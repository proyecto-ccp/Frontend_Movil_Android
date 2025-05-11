package com.uxdesign.ccp_frontend

data class Visita(
    val idCliente: String,
    val idVendedor: String,
    val fechaVisita: String,
    val motivo: String,
    val estado: String?
)
