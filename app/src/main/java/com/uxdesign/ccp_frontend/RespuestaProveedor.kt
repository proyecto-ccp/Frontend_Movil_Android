package com.uxdesign.ccp_frontend

data class RespuestaProveedor(
    val resultado: Int,
    val mensaje: String,
    val status: Int,
    val proveedores: List<Proveedor>
)
