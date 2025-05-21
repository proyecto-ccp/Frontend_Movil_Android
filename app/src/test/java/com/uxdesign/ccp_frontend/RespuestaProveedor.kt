package com.uxdesign.ccp_frontend

import org.junit.Assert.*
import org.junit.Test

class RespuestaProveedorTest {

    @Test
    fun `RespuestaProveedor debe asignar correctamente sus propiedades`() {
        // Crear una lista de proveedores de ejemplo con la estructura correcta
        val proveedores = listOf(
            Proveedor(
                id = "prov1",
                idTipoDocumento = 1,
                numeroDocumento = "12345678",
                nombre = "Proveedor 1",
                telefono = "555-1234",
                correo = "proveedor1@correo.com",
                direccion = "Dirección 1",
                idCiudad = "ciudad1",
                fechaCreacion = "2025-01-01",
                fechaModificacion = "2025-01-10"
            ),
            Proveedor(
                id = "prov2",
                idTipoDocumento = 2,
                numeroDocumento = "87654321",
                nombre = "Proveedor 2",
                telefono = "555-5678",
                correo = "proveedor2@correo.com",
                direccion = "Dirección 2",
                idCiudad = "ciudad2",
                fechaCreacion = "2025-02-01",
                fechaModificacion = "2025-02-15"
            )
        )

        // Crear una instancia de RespuestaProveedor con valores de ejemplo
        val respuestaProveedor = RespuestaProveedor(
            resultado = 1,
            mensaje = "Operación exitosa",
            status = 200,
            proveedores = proveedores
        )

        // Verificar que las propiedades se asignaron correctamente
        assertEquals(1, respuestaProveedor.resultado)
        assertEquals("Operación exitosa", respuestaProveedor.mensaje)
        assertEquals(200, respuestaProveedor.status)
        assertEquals(2, respuestaProveedor.proveedores.size)  // Debe contener 2 proveedores

        // Verificar el primer proveedor
        val proveedor1 = respuestaProveedor.proveedores[0]
        assertEquals("prov1", proveedor1.id)
        assertEquals(1, proveedor1.idTipoDocumento)
        assertEquals("12345678", proveedor1.numeroDocumento)
        assertEquals("Proveedor 1", proveedor1.nombre)
        assertEquals("555-1234", proveedor1.telefono)
        assertEquals("proveedor1@correo.com", proveedor1.correo)
        assertEquals("Dirección 1", proveedor1.direccion)
        assertEquals("ciudad1", proveedor1.idCiudad)
        assertEquals("2025-01-01", proveedor1.fechaCreacion)
        assertEquals("2025-01-10", proveedor1.fechaModificacion)

        // Verificar el segundo proveedor
        val proveedor2 = respuestaProveedor.proveedores[1]
        assertEquals("prov2", proveedor2.id)
        assertEquals(2, proveedor2.idTipoDocumento)
        assertEquals("87654321", proveedor2.numeroDocumento)
        assertEquals("Proveedor 2", proveedor2.nombre)
        assertEquals("555-5678", proveedor2.telefono)
        assertEquals("proveedor2@correo.com", proveedor2.correo)
        assertEquals("Dirección 2", proveedor2.direccion)
        assertEquals("ciudad2", proveedor2.idCiudad)
        assertEquals("2025-02-01", proveedor2.fechaCreacion)
        assertEquals("2025-02-15", proveedor2.fechaModificacion)
    }
}
