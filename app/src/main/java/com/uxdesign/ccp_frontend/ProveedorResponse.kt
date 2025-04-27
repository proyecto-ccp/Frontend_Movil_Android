package com.uxdesign.ccp_frontend

data class ProveedorResponse(
    val proveedores: List<Proveedor>,
    val resultado: Int,
    val mensaje: String,
    val status: Int
)
