package com.uxdesign.ccp_frontend

import org.junit.Assert.*
import org.junit.Test

class PedidoProcesadoTest {

    @Test
    fun `deberia crear una instancia valida de PedidoProcesado`() {
        // Crear una instancia del objeto PedidoProcesado
        val pedidoProcesado = PedidoProcesado(
            id = "PED12345",
            idCliente = "CLI98765",
            fechaRealizado = "2025-05-21",
            fechaEntrega = "2025-05-25",
            estadoPedido = "Procesado",
            valorTotal = 150.75,
            idVendedor = "VEN56789",
            comentarios = "Entrega urgente",
            idMoneda = 1
        )

        // Verificar que los valores sean correctos
        assertEquals("PED12345", pedidoProcesado.id)
        assertEquals("CLI98765", pedidoProcesado.idCliente)
        assertEquals("2025-05-21", pedidoProcesado.fechaRealizado)
        assertEquals("2025-05-25", pedidoProcesado.fechaEntrega)
        assertEquals("Procesado", pedidoProcesado.estadoPedido)
        assertEquals(150.75, pedidoProcesado.valorTotal, 0.001)
        assertEquals("VEN56789", pedidoProcesado.idVendedor)
        assertEquals("Entrega urgente", pedidoProcesado.comentarios)
        assertEquals(1, pedidoProcesado.idMoneda)
    }

    @Test
    fun `deberia crear una instancia de PedidoProcesado con comentarios vacíos`() {
        // Crear una instancia del objeto PedidoProcesado con comentarios vacíos
        val pedidoProcesado = PedidoProcesado(
            id = "PED54321",
            idCliente = "CLI12345",
            fechaRealizado = "2025-05-22",
            fechaEntrega = "2025-05-26",
            estadoPedido = "Pendiente",
            valorTotal = 250.50,
            idVendedor = "VEN98765",
            comentarios = "",  // Comentarios vacíos
            idMoneda = 2
        )

        // Verificar que los valores sean correctos y que los comentarios sean vacíos
        assertEquals("PED54321", pedidoProcesado.id)
        assertEquals("CLI12345", pedidoProcesado.idCliente)
        assertEquals("2025-05-22", pedidoProcesado.fechaRealizado)
        assertEquals("2025-05-26", pedidoProcesado.fechaEntrega)
        assertEquals("Pendiente", pedidoProcesado.estadoPedido)
        assertEquals(250.50, pedidoProcesado.valorTotal, 0.001)
        assertEquals("VEN98765", pedidoProcesado.idVendedor)
        assertEquals("", pedidoProcesado.comentarios) // Comentarios vacíos
        assertEquals(2, pedidoProcesado.idMoneda)
    }
}
