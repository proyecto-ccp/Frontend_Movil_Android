package com.uxdesign.ccp_frontend

data class LoginRequest(
    val username: String,
    val contrasena: String,
    val aplicacion: Int
)
