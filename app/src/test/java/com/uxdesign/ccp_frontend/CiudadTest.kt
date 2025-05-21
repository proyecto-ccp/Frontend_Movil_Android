import com.uxdesign.ccp_frontend.Ciudad
import org.junit.Assert.*
import org.junit.Test

class CiudadTest {

    @Test
    fun `deberia crear una instancia valida de Ciudad`() {
        val ciudad = Ciudad(
            id = "1",
            nombre = "Ciudad Test",
            poblacion = 100000
        )

        // Verificación de los valores de Ciudad
        assertEquals("1", ciudad.id)
        assertEquals("Ciudad Test", ciudad.nombre)
        assertEquals(100000, ciudad.poblacion)
    }

    @Test
    fun `deberia crear una instancia de Ciudad con poblacion 0`() {
        val ciudad = Ciudad(
            id = "2",
            nombre = "Ciudad Vacía",
            poblacion = 0
        )

        // Verificación de los valores de Ciudad con poblacion 0
        assertEquals("2", ciudad.id)
        assertEquals("Ciudad Vacía", ciudad.nombre)
        assertEquals(0, ciudad.poblacion)
    }

    @Test
    fun `deberia crear una instancia de Ciudad con poblacion negativa`() {
        val ciudad = Ciudad(
            id = "3",
            nombre = "Ciudad Negativa",
            poblacion = -5000
        )

        // Verificación de los valores de Ciudad con poblacion negativa
        assertEquals("3", ciudad.id)
        assertEquals("Ciudad Negativa", ciudad.nombre)
        assertEquals(-5000, ciudad.poblacion)
    }
}
