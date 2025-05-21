import com.uxdesign.ccp_frontend.*
import org.junit.Assert.*
import org.junit.Test

class RespuestaClienteTest {

    @Test
    fun `deberia crear una instancia valida de RespuestaCliente`() {
        val cliente = Cliente(
            id = "c1",
            nombre = "Juan",
            apellido = "Pérez",
            documento = "12345678",
            tipoDocumento = "DNI",
            telefono = "3001234567",
            email = "juan@correo.com",
            direccion = "Calle 123",
            ciudad = "Bogotá",
            idZona = "z1",
            zona = "Zona Norte"
        )

        val respuesta = RespuestaCliente(
            resultado = 1,
            mensaje = "Clientes cargados correctamente",
            status = 200,
            clientes = listOf(cliente)
        )

        assertEquals(1, respuesta.resultado)
        assertEquals("Clientes cargados correctamente", respuesta.mensaje)
        assertEquals(200, respuesta.status)
        assertEquals(1, respuesta.clientes.size)
        assertEquals("Juan", respuesta.clientes[0].nombre)
        assertEquals("Zona Norte", respuesta.clientes[0].zona)
    }

    @Test
    fun `deberia manejar lista vacia de clientes`() {
        val respuesta = RespuestaCliente(
            resultado = 0,
            mensaje = "No hay clientes disponibles",
            status = 204,
            clientes = emptyList()
        )

        assertEquals(0, respuesta.resultado)
        assertEquals("No hay clientes disponibles", respuesta.mensaje)
        assertEquals(204, respuesta.status)
        assertTrue(respuesta.clientes.isEmpty())
    }
}
