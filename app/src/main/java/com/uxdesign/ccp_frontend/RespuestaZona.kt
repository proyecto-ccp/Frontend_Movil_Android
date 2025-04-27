package com.uxdesign.ccp_frontend

data class RespuestaZona(
    val resultado: Int,
    val mensaje: String,
    val status: Int,
    val zonas: List<Zona>
)
