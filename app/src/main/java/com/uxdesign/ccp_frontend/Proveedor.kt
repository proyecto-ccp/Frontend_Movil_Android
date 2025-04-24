package com.uxdesign.ccp_frontend

data class Proveedor(
    val id: String,
    val idTipoDocumento: Int,
    val numeroDocumento: String,
    val nombre: String,
    val telefono: String,
    val correo: String,
    val direccion: String,
    val idCiudad: String,
    val fechaCreacion: String,
    val fechaModificacion: String
)
