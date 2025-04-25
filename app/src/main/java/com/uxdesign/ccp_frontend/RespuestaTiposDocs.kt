package com.uxdesign.ccp_frontend

data class RespuestaTiposDocs(
    val resultado: Int,
    val mensaje: String,
    val status: Int,
    val documentos: List<TipoDocumento>
)
