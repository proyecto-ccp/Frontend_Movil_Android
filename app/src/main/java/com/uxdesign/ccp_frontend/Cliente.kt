package com.uxdesign.ccp_frontend

import java.io.Serializable

data class Cliente(
    val id: String,
    val nombre: String,
    val apellido: String,
    val documento: String,
    val tipoDocumento: String,
    val telefono: String,
    val email: String,
    val direccion: String,
    val ciudad: String,
    val idZona: String,
    val zona: String
    //val contrasenia: String

): Serializable
