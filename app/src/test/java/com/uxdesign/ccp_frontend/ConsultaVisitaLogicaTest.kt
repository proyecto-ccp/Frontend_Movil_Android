package com.uxdesign.ccp_frontend.logic

import org.junit.Assert.*
import org.junit.Test

class ConsultaVisitaLogicTest {

    private val logica = ConsultaVisitaLogic()

    @Test
    fun `retorna error cuando el ID de usuario es nulo`() {
        val resultado = logica.validarDatos(null, "2025-05-15")
        assertTrue(resultado is ConsultaVisitaLogic.ResultadoValidacion.Error)
        assertEquals("ID de usuario no disponible", (resultado as ConsultaVisitaLogic.ResultadoValidacion.Error).mensaje)
    }

    @Test
    fun `retorna error cuando la fecha está vacía`() {
        val resultado = logica.validarDatos("123", "")
        assertTrue(resultado is ConsultaVisitaLogic.ResultadoValidacion.Error)
        assertEquals("Debes ingresar una fecha", (resultado as ConsultaVisitaLogic.ResultadoValidacion.Error).mensaje)
    }

    @Test
    fun `retorna exito cuando los datos son válidos`() {
        val resultado = logica.validarDatos("123", "2025-05-15")
        assertTrue(resultado is ConsultaVisitaLogic.ResultadoValidacion.Exito)
    }
}
