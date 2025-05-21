import com.uxdesign.ccp_frontend.RespuestaVendedor
import com.uxdesign.ccp_frontend.Vendedor
import org.junit.Assert.*
import org.junit.Test

class RespuestaVendedorTest {

    @Test
    fun `deberia crear una instancia valida de RespuestaVendedor`() {
        val vendedor = Vendedor(
            id = "v1",
            idTipoDocumento = 1,
            numeroDocumento = "12345678",
            nombre = "Carlos",
            apellido = "Sánchez",
            telefono = "987654321",
            correo = "carlos.sanchez@empresa.com",
            direccion = "Av. Siempre Viva 123",
            idzona = "zona1"
        )

        val respuesta = RespuestaVendedor(
            resultado = 1,
            mensaje = "Vendedor encontrado",
            status = 200,
            vendedor = vendedor
        )

        assertEquals(1, respuesta.resultado)
        assertEquals("Vendedor encontrado", respuesta.mensaje)
        assertEquals(200, respuesta.status)
        assertEquals(vendedor, respuesta.vendedor)
    }

    @Test
    fun `deberia crear un vendedor con datos correctos`() {
        val vendedor = Vendedor(
            id = "v1",
            idTipoDocumento = 1,
            numeroDocumento = "12345678",
            nombre = "Carlos",
            apellido = "Sánchez",
            telefono = "987654321",
            correo = "carlos.sanchez@empresa.com",
            direccion = "Av. Siempre Viva 123",
            idzona = "zona1"
        )

        assertEquals("v1", vendedor.id)
        assertEquals(1, vendedor.idTipoDocumento)
        assertEquals("12345678", vendedor.numeroDocumento)
        assertEquals("Carlos", vendedor.nombre)
        assertEquals("Sánchez", vendedor.apellido)
        assertEquals("987654321", vendedor.telefono)
        assertEquals("carlos.sanchez@empresa.com", vendedor.correo)
        assertEquals("Av. Siempre Viva 123", vendedor.direccion)
        assertEquals("zona1", vendedor.idzona)
    }
}
