import com.uxdesign.ccp_frontend.Zona
import org.junit.Assert.*
import org.junit.Test

class ZonaTest {

    @Test
    fun `deberia crear correctamente una zona`() {
        val zona = Zona(
            id = "z1",
            idCiudad = "c1",
            ciudad = "Bogotá",
            nombre = "Zona Norte",
            limites = "calle 100 a 200"
        )

        assertEquals("z1", zona.id)
        assertEquals("c1", zona.idCiudad)
        assertEquals("Bogotá", zona.ciudad)
        assertEquals("Zona Norte", zona.nombre)
        assertEquals("calle 100 a 200", zona.limites)
    }

    @Test
    fun `zonas iguales deben ser iguales`() {
        val zona1 = Zona("z1", "c1", "Bogotá", "Zona Norte", "calle 100 a 200")
        val zona2 = Zona("z1", "c1", "Bogotá", "Zona Norte", "calle 100 a 200")

        assertEquals(zona1, zona2) // usa equals de data class
    }

    @Test
    fun `zonas diferentes no deben ser iguales`() {
        val zona1 = Zona("z1", "c1", "Bogotá", "Zona Norte", "calle 100 a 200")
        val zona2 = Zona("z2", "c1", "Bogotá", "Zona Sur", "calle 0 a 100")

        assertNotEquals(zona1, zona2)
    }
}
