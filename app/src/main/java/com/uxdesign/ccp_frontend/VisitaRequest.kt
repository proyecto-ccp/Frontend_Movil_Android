package com.uxdesign.ccp_frontend

import java.io.Serializable

data class VisitaRequest(
    val id: Int,
    val idCliente: String,
    val idVendedor: String,
    val cliente: Cliente,
    val fechaVisita: String?,
    val motivo: String,
    val resultado: String,
    val estado: String
) : Serializable
