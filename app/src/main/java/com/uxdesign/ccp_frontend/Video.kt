package com.uxdesign.ccp_frontend

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Video(
    val id: String,
    val idCliente: String,
    val idProducto: String,
    val nombre: String,
    val urlVideo: String,
    val urlImagen: String?,
    val estadoCarga: String
) : Parcelable