package com.uxdesign.ccp_frontend

data class RespuestaVisita(
    val id: String?,
    val resultado: Int,
    val mensaje: String,
    val status: Int,
    val visitas: List<VisitaRequest>
)
