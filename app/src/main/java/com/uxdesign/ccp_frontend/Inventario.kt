package com.uxdesign.ccp_frontend

data class Inventario(
    val id: String,
    val idProducto: Int,
    val cantidadStock: Int,
    val fechaCreacion: String,
    val fechaModificacion: String
)
