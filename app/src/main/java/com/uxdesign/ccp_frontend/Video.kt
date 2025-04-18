package com.uxdesign.ccp_frontend

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Video(
    val id: String,
    val idCliente: String,
    val idProducto: Int,
    val nombre: String,
    val urlVideo: String,
    val urlImagen: String?, // puede ser null
    val estadoCarga: String
) : Parcelable