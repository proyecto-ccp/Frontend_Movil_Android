package com.uxdesign.ccp_frontend.logic

import com.uxdesign.ccp_frontend.ConsultaVisitaLogica
import org.junit.Assert.*
import org.junit.Test

class ConsultaVisitaLogicaTest {

    private val logica = ConsultaVisitaLogica()

    @Test
    fun `retorna error cuando el ID de usuario es nulo`() {
        val resultado = logica.validarDatos(null, "2025-05-15")
        assertTrue(resultado is ConsultaVisitaLogica.ResultadoValidacion.Error)
        assertEquals("ID de usuario no disponible", (resultado as ConsultaVisitaLogica.ResultadoValidacion.Error).mensaje)
    }

    @Test
    fun `retorna error cuando la fecha está vacía`() {
        val resultado = logica.validarDatos("123", "")
        assertTrue(resultado is ConsultaVisitaLogica.ResultadoValidacion.Error)
        assertEquals("Debes ingresar una fecha", (resultado as ConsultaVisitaLogica.ResultadoValidacion.Error).mensaje)
    }

    @Test
    fun `retorna exito cuando los datos son válidos`() {
        val resultado = logica.validarDatos("123", "2025-06-15")

        // Verificar que el resultado sea de tipo Exito
        assertTrue(resultado is ConsultaVisitaLogica.ResultadoValidacion.Exito)

        // Comprobar que el mensaje dentro de Exito sea el esperado
        val exito = resultado as ConsultaVisitaLogica.ResultadoValidacion.Exito
        assertEquals("Datos válidos", exito.mensaje)
    }
}
