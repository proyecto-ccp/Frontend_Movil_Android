package com.uxdesign.ccp_frontend

import java.io.Serializable

data class ClienteVisita(
    val nombre: String,
    val apellido: String,
    val tipoDocumento: String,
    val documento: String,
    val direccion: String,
    val telefono: String,
    val email: String,
    val zona: String?,
    val ciudad: String?
) : Serializable
