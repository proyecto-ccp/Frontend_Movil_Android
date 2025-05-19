package com.uxdesign.ccp_frontend

data class RespuestaUsuario(
    val resultado: Int,
    val mensaje: String,
    val status: Int,
    val id: String,
    val username: String,
    val nombres: String,
    val apellidos: String,
    val correo: String,
    val telefono: String,
    val idRol: String,
    val rol: String,
    val idPerfil: String,
    val fechaRegistro: String,
    val fechaActualizacion: String
)
