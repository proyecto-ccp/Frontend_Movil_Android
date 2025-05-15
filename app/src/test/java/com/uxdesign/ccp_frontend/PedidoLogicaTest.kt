package com.uxdesign.ccp_frontend

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import java.text.SimpleDateFormat
import java.util.*

class PedidoLogicTest {

    private lateinit var pedidoLogic: PedidoLogic
    private lateinit var mockApiService: ApiService

    @Before
    fun setup() {
        mockApiService = mock(ApiService::class.java)
        pedidoLogic = PedidoLogic(mockApiService)
    }

    @Test
    fun `validarCampos debe retornar true cuando todo es correcto`() {
        val result = pedidoLogic.validarCampos(
            fecha = "2025-05-15",
            numProductos = "3",
            total = "150.00",
            clienteSeleccionado = 1
        )
        assertTrue(result)
    }

    @Test
    fun `validarCampos debe retornar false si la fecha está vacía`() {
        val result = pedidoLogic.validarCampos(
            fecha = "",
            numProductos = "3",
            total = "150.00",
            clienteSeleccionado = 1
        )
        assertFalse(result)
    }

    @Test
    fun `validarCampos debe retornar false si clienteSeleccionado es 0`() {
        val result = pedidoLogic.validarCampos(
            fecha = "2025-05-15",
            numProductos = "3",
            total = "150.00",
            clienteSeleccionado = 0
        )
        assertFalse(result)
    }

    @Test
    fun `validarCampos debe retornar false si total está vacío`() {
        val result = pedidoLogic.validarCampos(
            fecha = "2025-05-15",
            numProductos = "3",
            total = "",
            clienteSeleccionado = 1
        )
        assertFalse(result)
    }

    @Test
    fun `convertirFechaAISO8601 debe convertir correctamente`() {
        val inputDate = "2025-05-15"
        val result = pedidoLogic.convertirFechaAISO8601(inputDate)

        assertTrue(result.startsWith("2025-05-15T"))
        assertTrue(result.endsWith("Z"))
    }

    @Test
    fun `convertirFechaAISO8601 debe generar una fecha UTC`() {
        val inputDate = "2025-05-15"
        val result = pedidoLogic.convertirFechaAISO8601(inputDate)

        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        val date = sdf.parse(result)
        assertNotNull(date)
    }
}
