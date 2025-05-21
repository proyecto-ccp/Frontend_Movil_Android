package com.uxdesign.ccp_frontend

import org.junit.Assert.*
import org.junit.Test

class InventarioTest {

    @Test
    fun `deberia crear una instancia valida de Inventario`() {
        // Crear una instancia de Inventario con datos de ejemplo
        val inventario = Inventario(
            id = "1",
            idProducto = 101,
            cantidadStock = 50,
            fechaCreacion = "2025-05-21",
            fechaModificacion = "2025-05-21"
        )

        // Verificar que los valores sean correctos
        assertEquals("1", inventario.id)
        assertEquals(101, inventario.idProducto)
        assertEquals(50, inventario.cantidadStock)
        assertEquals("2025-05-21", inventario.fechaCreacion)
        assertEquals("2025-05-21", inventario.fechaModificacion)
    }

    @Test
    fun `deberia crear una instancia de Inventario con valores por defecto`() {
        // Crear una instancia de Inventario con valores por defecto
        val inventario = Inventario(
            id = "",
            idProducto = 0,
            cantidadStock = 0,
            fechaCreacion = "",
            fechaModificacion = ""
        )

        // Verificar que los valores sean correctos, incluso si están vacíos o por defecto
        assertEquals("", inventario.id)
        assertEquals(0, inventario.idProducto)
        assertEquals(0, inventario.cantidadStock)
        assertEquals("", inventario.fechaCreacion)
        assertEquals("", inventario.fechaModificacion)
    }
}
