import com.uxdesign.ccp_frontend.LoginRequest
import org.junit.Assert.*
import org.junit.Test

class LoginRequestTest {

    @Test
    fun `deberia crear una instancia valida de LoginRequest`() {
        val loginRequest = LoginRequest(
            username = "usuario_test",
            contrasena = "contrasena_segura",
            aplicacion = 1
        )

        // Verificación de los valores de LoginRequest
        assertEquals("usuario_test", loginRequest.username)
        assertEquals("contrasena_segura", loginRequest.contrasena)
        assertEquals(1, loginRequest.aplicacion)
    }

    @Test
    fun `deberia crear una instancia de LoginRequest con aplicacion 0`() {
        val loginRequest = LoginRequest(
            username = "usuario_test",
            contrasena = "contrasena_segura",
            aplicacion = 0
        )

        // Verificación de los valores de LoginRequest con aplicacion 0
        assertEquals("usuario_test", loginRequest.username)
        assertEquals("contrasena_segura", loginRequest.contrasena)
        assertEquals(0, loginRequest.aplicacion)
    }

    @Test
    fun `deberia crear una instancia de LoginRequest sin contrasena`() {
        val loginRequest = LoginRequest(
            username = "usuario_test",
            contrasena = "",
            aplicacion = 1
        )

        // Verificación de los valores de LoginRequest con contrasena vacía
        assertEquals("usuario_test", loginRequest.username)
        assertEquals("", loginRequest.contrasena)
        assertEquals(1, loginRequest.aplicacion)
    }
}
