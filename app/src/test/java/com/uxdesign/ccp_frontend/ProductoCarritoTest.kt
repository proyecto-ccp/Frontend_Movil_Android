package com.uxdesign.ccp_frontend

import org.junit.Assert.*
import org.junit.Test

class ProductoCarritoTest {

    @Test
    fun `deberia crear una instancia valida de ProductoCarrito`() {
        val producto = ProductoCarrito(
            idProducto = 101,
            cantidad = 3,
            idUsuario = "user123",
            precioUnitario = 25.5
        )

        assertEquals(101, producto.idProducto)
        assertEquals(3, producto.cantidad)
        assertEquals("user123", producto.idUsuario)
        assertEquals(25.5, producto.precioUnitario, 0.001)
    }

    @Test
    fun `deberia calcular el total correctamente`() {
        val producto = ProductoCarrito(
            idProducto = 202,
            cantidad = 4,
            idUsuario = "user456",
            precioUnitario = 12.75
        )

        val totalEsperado = producto.cantidad * producto.precioUnitario
        assertEquals(51.0, totalEsperado, 0.001)
    }
}
