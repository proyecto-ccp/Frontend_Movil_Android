package com.uxdesign.ccp_frontend

import org.junit.Assert.*
import org.junit.Test

class RespuestaVisitaTest {

    @Test
    fun `deberia crear una instancia valida de RespuestaVisita`() {
        // Crear una lista de visitas de ejemplo
        val visitas = listOf(
            VisitaRequest(
                id = 1,
                idCliente = "cliente1",
                idVendedor = "vendedor1",
                cliente = Cliente(
                    id = "cliente1",
                    nombre = "Cliente Uno",
                    apellido = "Apellido Uno",
                    documento = "12345678",
                    tipoDocumento = "DNI",
                    telefono = "987654321",
                    email = "cliente1@correo.com",
                    direccion = "Calle Falsa 123",
                    ciudad = "Madrid",
                    idZona = "zona1",
                    zona = "Zona 1"
                ),
                fechaVisita = "2025-05-21",
                motivo = "Motivo de la visita",
                resultado = "Resultado de la visita",
                estado = "PENDIENTE"
            )
        )

        // Crear una instancia de RespuestaVisita
        val respuestaVisita = RespuestaVisita(
            id = "123",
            resultado = 1,
            mensaje = "Éxito",
            status = 200,
            visitas = visitas
        )

        // Verificar que los valores sean correctos
        assertEquals("123", respuestaVisita.id)
        assertEquals(1, respuestaVisita.resultado)
        assertEquals("Éxito", respuestaVisita.mensaje)
        assertEquals(200, respuestaVisita.status)
        assertEquals(1, respuestaVisita.visitas.size)
        assertEquals("cliente1", respuestaVisita.visitas[0].idCliente)
    }

    @Test
    fun `deberia manejar id nulo en RespuestaVisita`() {
        // Crear una lista de visitas de ejemplo
        val visitas = listOf(
            VisitaRequest(
                id = 2,
                idCliente = "cliente2",
                idVendedor = "vendedor2",
                cliente = Cliente(
                    id = "cliente2",
                    nombre = "Cliente Dos",
                    apellido = "Apellido Dos",
                    documento = "87654321",
                    tipoDocumento = "DNI",
                    telefono = "123456789",
                    email = "cliente2@correo.com",
                    direccion = "Av. Libertad 456",
                    ciudad = "Barcelona",
                    idZona = "zona2",
                    zona = "Zona 2"
                ),
                fechaVisita = "2025-05-22",
                motivo = "Motivo de la visita",
                resultado = "Resultado de la visita",
                estado = "PENDIENTE"
            )
        )

        // Crear una instancia de RespuestaVisita con id nulo
        val respuestaVisita = RespuestaVisita(
            id = null,
            resultado = 1,
            mensaje = "Éxito",
            status = 200,
            visitas = visitas
        )

        // Verificar que el id es nulo
        assertNull(respuestaVisita.id)
        assertEquals(1, respuestaVisita.resultado)
        assertEquals("Éxito", respuestaVisita.mensaje)
        assertEquals(200, respuestaVisita.status)
        assertEquals(1, respuestaVisita.visitas.size)
        assertEquals("cliente2", respuestaVisita.visitas[0].idCliente)
    }
}
