import com.uxdesign.ccp_frontend.Visita
import org.junit.Assert.*
import org.junit.Test

class VisitaTest {

    @Test
    fun `deberia crear una instancia valida de Visita`() {
        val visita = Visita(
            idCliente = "cliente1",
            idVendedor = "vendedor1",
            fechaVisita = "2023-05-21",
            motivo = "Seguimiento de pedido",
            estado = "Confirmada"
        )

        // Verificación de los valores en Visita
        assertEquals("cliente1", visita.idCliente)
        assertEquals("vendedor1", visita.idVendedor)
        assertEquals("2023-05-21", visita.fechaVisita)
        assertEquals("Seguimiento de pedido", visita.motivo)
        assertEquals("Confirmada", visita.estado)
    }

    @Test
    fun `deberia crear una instancia de Visita con estado nulo`() {
        val visita = Visita(
            idCliente = "cliente2",
            idVendedor = "vendedor2",
            fechaVisita = "2023-05-22",
            motivo = "Consulta sobre producto",
            estado = null
        )

        // Verificación de los valores en Visita con estado nulo
        assertEquals("cliente2", visita.idCliente)
        assertEquals("vendedor2", visita.idVendedor)
        assertEquals("2023-05-22", visita.fechaVisita)
        assertEquals("Consulta sobre producto", visita.motivo)
        assertNull(visita.estado)
    }

    @Test
    fun `deberia crear una instancia de Visita con valores nulos opcionales`() {
        val visita = Visita(
            idCliente = "cliente3",
            idVendedor = "vendedor3",
            fechaVisita = "2023-05-23",
            motivo = "Revisión de contrato",
            estado = null
        )

        // Verificación de los valores en Visita con estado nulo
        assertEquals("cliente3", visita.idCliente)
        assertEquals("vendedor3", visita.idVendedor)
        assertEquals("2023-05-23", visita.fechaVisita)
        assertEquals("Revisión de contrato", visita.motivo)
        assertNull(visita.estado)
    }
}
