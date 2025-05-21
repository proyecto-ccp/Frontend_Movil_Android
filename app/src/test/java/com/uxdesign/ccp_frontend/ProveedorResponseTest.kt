import com.uxdesign.ccp_frontend.Proveedor
import com.uxdesign.ccp_frontend.ProveedorResponse
import org.junit.Assert.*
import org.junit.Test

class ProveedorResponseTest {

    @Test
    fun `deberia crear una instancia valida de ProveedorResponse`() {
        val proveedor1 = Proveedor(
            id = "prov1",
            idTipoDocumento = 1,
            numeroDocumento = "12345678",
            nombre = "Proveedor A",
            telefono = "123456789",
            correo = "proveedorA@mail.com",
            direccion = "Calle Falsa 123",
            idCiudad = "ciudad1",
            fechaCreacion = "2023-01-01",
            fechaModificacion = "2023-01-02"
        )

        val proveedor2 = Proveedor(
            id = "prov2",
            idTipoDocumento = 2,
            numeroDocumento = "87654321",
            nombre = "Proveedor B",
            telefono = "987654321",
            correo = "proveedorB@mail.com",
            direccion = "Avenida Siempre Viva 456",
            idCiudad = "ciudad2",
            fechaCreacion = "2023-02-01",
            fechaModificacion = "2023-02-02"
        )

        val proveedorResponse = ProveedorResponse(
            proveedores = listOf(proveedor1, proveedor2),
            resultado = 1,
            mensaje = "Operación exitosa",
            status = 200
        )

        // Verificación de los valores en ProveedorResponse
        assertEquals(2, proveedorResponse.proveedores.size)
        assertEquals("prov1", proveedorResponse.proveedores[0].id)
        assertEquals("Proveedor A", proveedorResponse.proveedores[0].nombre)
        assertEquals("proveedorA@mail.com", proveedorResponse.proveedores[0].correo)

        assertEquals(1, proveedorResponse.resultado)
        assertEquals("Operación exitosa", proveedorResponse.mensaje)
        assertEquals(200, proveedorResponse.status)
    }

    @Test
    fun `deberia crear una instancia de ProveedorResponse con lista vacía de proveedores`() {
        val proveedorResponse = ProveedorResponse(
            proveedores = emptyList(),
            resultado = 1,
            mensaje = "Sin proveedores disponibles",
            status = 404
        )

        // Verificación de los valores en ProveedorResponse con lista vacía
        assertEquals(0, proveedorResponse.proveedores.size)
        assertEquals(1, proveedorResponse.resultado)
        assertEquals("Sin proveedores disponibles", proveedorResponse.mensaje)
        assertEquals(404, proveedorResponse.status)
    }

    @Test
    fun `deberia crear una instancia de ProveedorResponse con un solo proveedor`() {
        val proveedor = Proveedor(
            id = "prov1",
            idTipoDocumento = 1,
            numeroDocumento = "12345678",
            nombre = "Proveedor Único",
            telefono = "123456789",
            correo = "proveedorUnico@mail.com",
            direccion = "Calle Falsa 123",
            idCiudad = "ciudad1",
            fechaCreacion = "2023-01-01",
            fechaModificacion = "2023-01-02"
        )

        val proveedorResponse = ProveedorResponse(
            proveedores = listOf(proveedor),
            resultado = 1,
            mensaje = "Operación exitosa",
            status = 200
        )

        // Verificación de los valores en ProveedorResponse con un solo proveedor
        assertEquals(1, proveedorResponse.proveedores.size)
        assertEquals("Proveedor Único", proveedorResponse.proveedores[0].nombre)
        assertEquals("proveedorUnico@mail.com", proveedorResponse.proveedores[0].correo)
        assertEquals(1, proveedorResponse.resultado)
        assertEquals("Operación exitosa", proveedorResponse.mensaje)
        assertEquals(200, proveedorResponse.status)
    }
}
