package com.uxdesign.ccp_frontend

import org.junit.Assert.*
import org.junit.Test

class PedidoTest {

    @Test
    fun `deberia crear una instancia valida de Pedido`() {
        // Crear una instancia del objeto Pedido
        val pedido = Pedido(
            idCliente = "cliente123",
            fechaEntrega = "2025-06-15",
            estadoPedido = "Enviado",
            valorTotal = 250.75,
            idVendedor = "vendedor456",
            comentarios = "Entrega urgente",
            idMoneda = 1
        )

        // Verificar que los valores sean correctos
        assertEquals("cliente123", pedido.idCliente)
        assertEquals("2025-06-15", pedido.fechaEntrega)
        assertEquals("Enviado", pedido.estadoPedido)
        assertEquals(250.75, pedido.valorTotal, 0.001)
        assertEquals("vendedor456", pedido.idVendedor)
        assertEquals("Entrega urgente", pedido.comentarios)
        assertEquals(1, pedido.idMoneda)
    }

    @Test
    fun `deberia crear una instancia de Pedido con comentarios vacíos`() {
        // Crear una instancia del objeto Pedido con comentarios vacíos
        val pedido = Pedido(
            idCliente = "cliente789",
            fechaEntrega = "2025-07-01",
            estadoPedido = "Pendiente",
            valorTotal = 150.50,
            idVendedor = "vendedor123",
            comentarios = "",  // Comentarios vacíos
            idMoneda = 2
        )

        // Verificar que los valores sean correctos y que los comentarios estén vacíos
        assertEquals("cliente789", pedido.idCliente)
        assertEquals("2025-07-01", pedido.fechaEntrega)
        assertEquals("Pendiente", pedido.estadoPedido)
        assertEquals(150.50, pedido.valorTotal, 0.001)
        assertEquals("vendedor123", pedido.idVendedor)
        assertEquals("", pedido.comentarios)  // Comentarios vacíos
        assertEquals(2, pedido.idMoneda)
    }
}
