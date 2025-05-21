package com.uxdesign.ccp_frontend

import org.junit.Assert.*
import org.junit.Test

class RespuestaLoginTest {

    @Test
    fun `deberia crear una instancia valida de RespuestaLogin`() {
        // Crear una instancia del objeto RespuestaLogin
        val respuestaLogin = RespuestaLogin(
            resultado = 1,
            mensaje = "Login exitoso",
            status = 200,
            token = "abc123xyz456",
            menu = "principal",
            idusuario = "user12345"
        )

        // Verificar que los valores sean correctos
        assertEquals(1, respuestaLogin.resultado)
        assertEquals("Login exitoso", respuestaLogin.mensaje)
        assertEquals(200, respuestaLogin.status)
        assertEquals("abc123xyz456", respuestaLogin.token)
        assertEquals("principal", respuestaLogin.menu)
        assertEquals("user12345", respuestaLogin.idusuario)
    }

    @Test
    fun `deberia crear una instancia de RespuestaLogin con mensaje vacío`() {
        // Crear una instancia del objeto RespuestaLogin con un mensaje vacío
        val respuestaLogin = RespuestaLogin(
            resultado = 0,
            mensaje = "",  // Mensaje vacío
            status = 400,
            token = "xyz789abc012",
            menu = "login",
            idusuario = "user98765"
        )

        // Verificar que los valores sean correctos y que el mensaje esté vacío
        assertEquals(0, respuestaLogin.resultado)
        assertEquals("", respuestaLogin.mensaje) // Mensaje vacío
        assertEquals(400, respuestaLogin.status)
        assertEquals("xyz789abc012", respuestaLogin.token)
        assertEquals("login", respuestaLogin.menu)
        assertEquals("user98765", respuestaLogin.idusuario)
    }
}
