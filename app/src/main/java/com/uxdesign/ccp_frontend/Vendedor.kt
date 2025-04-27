package com.uxdesign.ccp_frontend

data class Vendedor(
    val id: String,
    val idTipoDocumento: Int,
    val numeroDocumento: String,
    val nombre: String,
    val apellido: String,
    val telefono: String,
    val correo: String,
    val direccion: String,
    val idzona: String
)
