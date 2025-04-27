package com.uxdesign.ccp_frontend

data class RespuestaCiudad (
    val resultado: Int,
    val mensaje: String,
    val status: Int,
    val ciudades: List<Ciudad>
)