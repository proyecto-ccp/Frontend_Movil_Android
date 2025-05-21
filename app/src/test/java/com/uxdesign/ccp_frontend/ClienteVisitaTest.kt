package com.uxdesign.ccp_frontend

import org.junit.Assert.*
import org.junit.Test

class ClienteVisitaTest {

    @Test
    fun `deberia crear una instancia valida de ClienteVisita`() {
        // Crear una instancia del objeto ClienteVisita
        val clienteVisita = ClienteVisita(
            nombre = "Juan",
            apellido = "Pérez",
            tipoDocumento = "DNI",
            documento = "12345678",
            direccion = "Calle Falsa 123",
            telefono = "987654321",
            email = "juan.perez@correo.com",
            zona = "Zona A",
            ciudad = "Ciudad X"
        )

        // Verificar que los valores sean correctos
        assertEquals("Juan", clienteVisita.nombre)
        assertEquals("Pérez", clienteVisita.apellido)
        assertEquals("DNI", clienteVisita.tipoDocumento)
        assertEquals("12345678", clienteVisita.documento)
        assertEquals("Calle Falsa 123", clienteVisita.direccion)
        assertEquals("987654321", clienteVisita.telefono)
        assertEquals("juan.perez@correo.com", clienteVisita.email)
        assertEquals("Zona A", clienteVisita.zona)
        assertEquals("Ciudad X", clienteVisita.ciudad)
    }

    @Test
    fun `deberia crear una instancia de ClienteVisita sin zona y ciudad`() {
        // Crear una instancia del objeto ClienteVisita con zona y ciudad como nulos
        val clienteVisita = ClienteVisita(
            nombre = "Ana",
            apellido = "Gómez",
            tipoDocumento = "DNI",
            documento = "87654321",
            direccion = "Av. Libertad 456",
            telefono = "123456789",
            email = "ana.gomez@correo.com",
            zona = null,  // Sin zona
            ciudad = null // Sin ciudad
        )

        // Verificar que los valores sean correctos y zona y ciudad sean nulos
        assertEquals("Ana", clienteVisita.nombre)
        assertEquals("Gómez", clienteVisita.apellido)
        assertEquals("DNI", clienteVisita.tipoDocumento)
        assertEquals("87654321", clienteVisita.documento)
        assertEquals("Av. Libertad 456", clienteVisita.direccion)
        assertEquals("123456789", clienteVisita.telefono)
        assertEquals("ana.gomez@correo.com", clienteVisita.email)
        assertNull(clienteVisita.zona) // Verificar que la zona es nula
        assertNull(clienteVisita.ciudad) // Verificar que la ciudad es nula
    }
}
