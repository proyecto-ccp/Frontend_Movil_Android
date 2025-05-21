package com.uxdesign.ccp_frontend

import com.uxdesign.ccp_frontend.Vendedor
import org.junit.Assert.*
import org.junit.Test

class VendedorTest {

    @Test
    fun `deberia crear una instancia valida de Vendedor`() {
        val vendedor = Vendedor(
            id = "v001",
            idTipoDocumento = 1,
            numeroDocumento = "12345678",
            nombre = "Carlos",
            apellido = "Ramírez",
            telefono = "3001234567",
            correo = "carlos.ramirez@example.com",
            direccion = "Calle 123 #45-67",
            idzona = "zona001"
        )

        assertEquals("v001", vendedor.id)
        assertEquals(1, vendedor.idTipoDocumento)
        assertEquals("12345678", vendedor.numeroDocumento)
        assertEquals("Carlos", vendedor.nombre)
        assertEquals("Ramírez", vendedor.apellido)
        assertEquals("3001234567", vendedor.telefono)
        assertEquals("carlos.ramirez@example.com", vendedor.correo)
        assertEquals("Calle 123 #45-67", vendedor.direccion)
        assertEquals("zona001", vendedor.idzona)
    }

    @Test
    fun `dos instancias con los mismos datos deben ser iguales`() {
        val v1 = Vendedor("v1", 1, "123", "Ana", "Gómez", "3011234567", "ana@example.com", "Calle 1", "z001")
        val v2 = Vendedor("v1", 1, "123", "Ana", "Gómez", "3011234567", "ana@example.com", "Calle 1", "z001")

        assertEquals(v1, v2)
    }

    @Test
    fun `instancias con datos distintos no deben ser iguales`() {
        val v1 = Vendedor("v1", 1, "123", "Ana", "Gómez", "3011234567", "ana@example.com", "Calle 1", "z001")
        val v2 = Vendedor("v2", 2, "456", "Luis", "Pérez", "3027654321", "luis@example.com", "Carrera 9", "z002")

        assertNotEquals(v1, v2)
    }
}
