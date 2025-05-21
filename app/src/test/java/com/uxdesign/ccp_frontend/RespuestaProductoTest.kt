package com.uxdesign.ccp_frontend

import org.junit.Assert.*
import org.junit.Test

class RespuestaProductoTest {

    @Test
    fun `deberia crear una instancia valida de RespuestaProducto`() {
        // Crear una lista de productos de ejemplo
        val productos = listOf(
            Producto(
                id = 1,
                nombre = "Producto A",
                descripcion = "Descripción del Producto A",
                idProveedor = "Proveedor1",
                precioUnitario = 50.0,
                cantidad = 10,
                idMedida = 1,
                idCategoria = 2,
                idMarca = 3,
                idColor = 4,
                idModelo = 5,
                idMaterial = 6,
                urlFoto1 = "urlFoto1",
                urlFoto2 = "urlFoto2"
            ),
            Producto(
                id = 2,
                nombre = "Producto B",
                descripcion = "Descripción del Producto B",
                idProveedor = "Proveedor2",
                precioUnitario = 30.0,
                cantidad = 5,
                idMedida = 2,
                idCategoria = 3,
                idMarca = 4,
                idColor = 5,
                idModelo = 6,
                idMaterial = 7,
                urlFoto1 = "urlFoto3",
                urlFoto2 = "urlFoto4"
            )
        )

        // Crear una instancia de RespuestaProducto
        val respuestaProducto = RespuestaProducto(
            resultado = 1,
            mensaje = "Productos obtenidos correctamente",
            status = 200,
            productos = productos
        )

        // Verificar que los valores sean correctos
        assertEquals(1, respuestaProducto.resultado)
        assertEquals("Productos obtenidos correctamente", respuestaProducto.mensaje)
        assertEquals(200, respuestaProducto.status)
        assertEquals(2, respuestaProducto.productos.size)  // Verificamos que haya dos productos
        assertEquals("Producto A", respuestaProducto.productos[0].nombre)
        assertEquals("Producto B", respuestaProducto.productos[1].nombre)
        assertEquals(50.0, respuestaProducto.productos[0].precioUnitario, 0.001)
        assertEquals(30.0, respuestaProducto.productos[1].precioUnitario, 0.001)
    }

    @Test
    fun `deberia manejar respuesta con lista vacia de productos`() {
        // Crear una lista vacía de productos
        val productosVacios = emptyList<Producto>()

        // Crear una instancia de RespuestaProducto con productos vacíos
        val respuestaProducto = RespuestaProducto(
            resultado = 0,
            mensaje = "No se encontraron productos",
            status = 404,
            productos = productosVacios
        )

        // Verificar que los valores sean correctos
        assertEquals(0, respuestaProducto.resultado)
        assertEquals("No se encontraron productos", respuestaProducto.mensaje)
        assertEquals(404, respuestaProducto.status)
        assertTrue(respuestaProducto.productos.isEmpty())  // Verificamos que la lista esté vacía
    }
}
