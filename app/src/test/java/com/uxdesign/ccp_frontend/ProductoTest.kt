package com.uxdesign.ccp_frontend

import org.junit.Assert.*
import org.junit.Test

class ProductoTest {

    @Test
    fun `deberia crear una instancia valida de Producto`() {
        // Crear una instancia del objeto Producto
        val producto = Producto(
            id = 1,
            nombre = "Producto A",
            descripcion = "Descripción del Producto A",
            idProveedor = "Proveedor A",
            precioUnitario = 100.0,
            cantidad = 5,
            idMedida = 1,
            idCategoria = 1,
            idMarca = 1,
            idColor = 1,
            idModelo = 1,
            idMaterial = 1,
            urlFoto1 = "http://example.com/foto1.jpg",
            urlFoto2 = "http://example.com/foto2.jpg"
        )

        // Verificar que los valores sean correctos
        assertEquals(1, producto.id)
        assertEquals("Producto A", producto.nombre)
        assertEquals("Descripción del Producto A", producto.descripcion)
        assertEquals("Proveedor A", producto.idProveedor)
        assertEquals(100.0, producto.precioUnitario, 0.001)
        assertEquals(5, producto.cantidad)
        assertEquals(1, producto.idMedida)
        assertEquals(1, producto.idCategoria)
        assertEquals(1, producto.idMarca)
        assertEquals(1, producto.idColor)
        assertEquals(1, producto.idModelo)
        assertEquals(1, producto.idMaterial)
        assertEquals("http://example.com/foto1.jpg", producto.urlFoto1)
        assertEquals("http://example.com/foto2.jpg", producto.urlFoto2)
    }
}
