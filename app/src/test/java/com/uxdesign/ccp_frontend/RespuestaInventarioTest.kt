package com.uxdesign.ccp_frontend

import org.junit.Assert.*
import org.junit.Test

class RespuestaInventarioTest {

    @Test
    fun `deberia crear una instancia valida de RespuestaInventario`() {
        // Crear una instancia de Inventario de ejemplo con los atributos correctos
        val inventario = Inventario(
            id = "1",
            idProducto = 101,
            cantidadStock = 100,
            fechaCreacion = "2025-05-21",
            fechaModificacion = "2025-05-21"
        )

        // Crear una instancia de RespuestaInventario
        val respuestaInventario = RespuestaInventario(
            resultado = 1,
            mensaje = "Inventario obtenido correctamente",
            status = 200,
            inventario = inventario
        )

        // Verificar que los valores sean correctos
        assertEquals(1, respuestaInventario.resultado)
        assertEquals("Inventario obtenido correctamente", respuestaInventario.mensaje)
        assertEquals(200, respuestaInventario.status)
        assertEquals("1", respuestaInventario.inventario.id)
        assertEquals(101, respuestaInventario.inventario.idProducto)
        assertEquals(100, respuestaInventario.inventario.cantidadStock)
        assertEquals("2025-05-21", respuestaInventario.inventario.fechaCreacion)
        assertEquals("2025-05-21", respuestaInventario.inventario.fechaModificacion)
    }

    @Test
    fun `deberia manejar respuesta con inventario vacio`() {
        // Crear una instancia de Inventario vacío de ejemplo
        val inventarioVacio = Inventario(
            id = "",
            idProducto = 0,
            cantidadStock = 0,
            fechaCreacion = "",
            fechaModificacion = ""
        )

        // Crear una instancia de RespuestaInventario con inventario vacío
        val respuestaInventario = RespuestaInventario(
            resultado = 0,
            mensaje = "No se encontraron productos en el inventario",
            status = 404,
            inventario = inventarioVacio
        )

        // Verificar que los valores sean correctos incluso con inventario vacío
        assertEquals(0, respuestaInventario.resultado)
        assertEquals("No se encontraron productos en el inventario", respuestaInventario.mensaje)
        assertEquals(404, respuestaInventario.status)
        assertEquals("", respuestaInventario.inventario.id)
        assertEquals(0, respuestaInventario.inventario.idProducto)
        assertEquals(0, respuestaInventario.inventario.cantidadStock)
        assertEquals("", respuestaInventario.inventario.fechaCreacion)
        assertEquals("", respuestaInventario.inventario.fechaModificacion)
    }
}
