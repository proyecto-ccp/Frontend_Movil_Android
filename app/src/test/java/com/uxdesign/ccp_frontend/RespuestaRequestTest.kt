package com.uxdesign.ccp_frontend

import org.junit.Assert.*
import org.junit.Test

class RespuestaRequestTest {

    @Test
    fun `deberia crear una instancia valida de RespuestaRequest`() {
        // Crear una instancia del objeto RespuestaRequest
        val respuesta = RespuestaRequest(
            id = "respuesta123",
            resultado = 1,
            mensaje = "Éxito",
            status = 200
        )

        // Verificar que los valores sean correctos
        assertEquals("respuesta123", respuesta.id)
        assertEquals(1, respuesta.resultado)
        assertEquals("Éxito", respuesta.mensaje)
        assertEquals(200, respuesta.status)
    }

    @Test
    fun `deberia crear una instancia de RespuestaRequest con status 404`() {
        // Crear una instancia del objeto RespuestaRequest con un status diferente
        val respuesta = RespuestaRequest(
            id = "respuesta404",
            resultado = 0,
            mensaje = "No encontrado",
            status = 404
        )

        // Verificar que los valores sean correctos
        assertEquals("respuesta404", respuesta.id)
        assertEquals(0, respuesta.resultado)
        assertEquals("No encontrado", respuesta.mensaje)
        assertEquals(404, respuesta.status)
    }
}
