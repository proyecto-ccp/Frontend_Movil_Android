import com.uxdesign.ccp_frontend.Proveedor
import org.junit.Assert.*
import org.junit.Test

class ProveedorTest {

    @Test
    fun `deberia crear una instancia valida de Proveedor`() {
        val proveedor = Proveedor(
            id = "p001",
            idTipoDocumento = 2,
            numeroDocumento = "900123456",
            nombre = "Proveedor XYZ",
            telefono = "3124567890",
            correo = "proveedor@xyz.com",
            direccion = "Cra 12 #34-56",
            idCiudad = "c001",
            fechaCreacion = "2024-01-01",
            fechaModificacion = "2024-06-01"
        )

        assertEquals("p001", proveedor.id)
        assertEquals(2, proveedor.idTipoDocumento)
        assertEquals("900123456", proveedor.numeroDocumento)
        assertEquals("Proveedor XYZ", proveedor.nombre)
        assertEquals("3124567890", proveedor.telefono)
        assertEquals("proveedor@xyz.com", proveedor.correo)
        assertEquals("Cra 12 #34-56", proveedor.direccion)
        assertEquals("c001", proveedor.idCiudad)
        assertEquals("2024-01-01", proveedor.fechaCreacion)
        assertEquals("2024-06-01", proveedor.fechaModificacion)
    }

    @Test
    fun `proveedores con los mismos datos deben ser iguales`() {
        val p1 = Proveedor("p1", 1, "123", "A", "300", "a@a.com", "dir", "c1", "2024-01-01", "2024-01-01")
        val p2 = Proveedor("p1", 1, "123", "A", "300", "a@a.com", "dir", "c1", "2024-01-01", "2024-01-01")

        assertEquals(p1, p2)
    }

    @Test
    fun `proveedores con datos distintos no deben ser iguales`() {
        val p1 = Proveedor("p1", 1, "123", "A", "300", "a@a.com", "dir", "c1", "2024-01-01", "2024-01-01")
        val p2 = Proveedor("p2", 2, "456", "B", "301", "b@b.com", "dir2", "c2", "2024-02-01", "2024-02-01")

        assertNotEquals(p1, p2)
    }
}
