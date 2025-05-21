import com.uxdesign.ccp_frontend.Cliente
import com.uxdesign.ccp_frontend.VisitaRequest
import org.junit.Assert.*
import org.junit.Test

class VisitaRequestTest {

    @Test
    fun `deberia crear una instancia valida de VisitaRequest`() {
        val cliente = Cliente(
            id = "c1",
            nombre = "Juan",
            apellido = "Pérez",
            documento = "12345678",
            tipoDocumento = "DNI",
            telefono = "321654987",
            email = "juan.perez@correo.com",
            direccion = "Calle Falsa 123",
            ciudad = "Ciudad X",
            idZona = "zona1",
            zona = "Zona A"
        )

        val visita = VisitaRequest(
            id = 1,
            idCliente = "c1",
            idVendedor = "v1",
            cliente = cliente,
            fechaVisita = "2025-05-20",
            motivo = "Reunión de seguimiento",
            resultado = "Visitado con éxito",
            estado = "Completado"
        )

        // Verificación de los valores de la VisitaRequest
        assertEquals(1, visita.id)
        assertEquals("c1", visita.idCliente)
        assertEquals("v1", visita.idVendedor)
        assertEquals(cliente, visita.cliente)
        assertEquals("2025-05-20", visita.fechaVisita)
        assertEquals("Reunión de seguimiento", visita.motivo)
        assertEquals("Visitado con éxito", visita.resultado)
        assertEquals("Completado", visita.estado)
    }

    @Test
    fun `deberia crear una instancia de VisitaRequest sin fecha de visita`() {
        val cliente = Cliente(
            id = "c1",
            nombre = "Juan",
            apellido = "Pérez",
            documento = "12345678",
            tipoDocumento = "DNI",
            telefono = "321654987",
            email = "juan.perez@correo.com",
            direccion = "Calle Falsa 123",
            ciudad = "Ciudad X",
            idZona = "zona1",
            zona = "Zona A"
        )

        val visita = VisitaRequest(
            id = 2,
            idCliente = "c1",
            idVendedor = "v2",
            cliente = cliente,
            fechaVisita = null, // Fecha de visita no asignada
            motivo = "Visita inicial",
            resultado = "Pendiente",
            estado = "Agendada"
        )

        // Verificación de los valores con fecha de visita null
        assertNull(visita.fechaVisita)
        assertEquals("Visita inicial", visita.motivo)
        assertEquals("Pendiente", visita.resultado)
        assertEquals("Agendada", visita.estado)
    }

    @Test
    fun `deberia validar los datos del cliente dentro de VisitaRequest`() {
        val cliente = Cliente(
            id = "c1",
            nombre = "Juan",
            apellido = "Pérez",
            documento = "12345678",
            tipoDocumento = "DNI",
            telefono = "321654987",
            email = "juan.perez@correo.com",
            direccion = "Calle Falsa 123",
            ciudad = "Ciudad X",
            idZona = "zona1",
            zona = "Zona A"
        )

        val visita = VisitaRequest(
            id = 3,
            idCliente = "c1",
            idVendedor = "v3",
            cliente = cliente,
            fechaVisita = "2025-05-21",
            motivo = "Seguimiento de venta",
            resultado = "Visitado con éxito",
            estado = "Completado"
        )

        // Verificación de los datos del cliente dentro de la VisitaRequest
        assertEquals("Juan", visita.cliente.nombre)
        assertEquals("Pérez", visita.cliente.apellido)
        assertEquals("12345678", visita.cliente.documento)
        assertEquals("DNI", visita.cliente.tipoDocumento)
        assertEquals("321654987", visita.cliente.telefono)
        assertEquals("juan.perez@correo.com", visita.cliente.email)
        assertEquals("Calle Falsa 123", visita.cliente.direccion)
        assertEquals("Ciudad X", visita.cliente.ciudad)
        assertEquals("zona1", visita.cliente.idZona)
        assertEquals("Zona A", visita.cliente.zona)
    }
}
