package com.uxdesign.ccp_frontend

data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val idProveedor: String,
    val precioUnitario: Double,
    val cantidad: Int,
    val idMedida: Int,
    val idCategoria: Int,
    val idMarca: Int,
    val idColor: Int,
    val idModelo: Int,
    val idMaterial: Int,
    val urlFoto1: String,
    val urlFoto2: String
)