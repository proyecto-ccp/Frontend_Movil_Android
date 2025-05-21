import com.uxdesign.ccp_frontend.RespuestaUsuario
import org.junit.Assert.*
import org.junit.Test

class RespuestaUsuarioTest {

    @Test
    fun `deberia crear una instancia correcta de RespuestaUsuario`() {
        val respuesta = RespuestaUsuario(
            resultado = 1,
            mensaje = "Inicio de sesión exitoso",
            status = 200,
            id = "123",
            username = "usuario1",
            nombres = "Juan",
            apellidos = "Pérez",
            correo = "juan.perez@example.com",
            telefono = "3210000000",
            idRol = "admin",
            rol = "Administrador",
            idPerfil = "perfil123",
            fechaRegistro = "2023-01-01",
            fechaActualizacion = "2023-05-01"
        )

        assertEquals(1, respuesta.resultado)
        assertEquals("Inicio de sesión exitoso", respuesta.mensaje)
        assertEquals(200, respuesta.status)
        assertEquals("123", respuesta.id)
        assertEquals("usuario1", respuesta.username)
        assertEquals("Juan", respuesta.nombres)
        assertEquals("Pérez", respuesta.apellidos)
        assertEquals("juan.perez@example.com", respuesta.correo)
        assertEquals("3210000000", respuesta.telefono)
        assertEquals("admin", respuesta.idRol)
        assertEquals("Administrador", respuesta.rol)
        assertEquals("perfil123", respuesta.idPerfil)
        assertEquals("2023-01-01", respuesta.fechaRegistro)
        assertEquals("2023-05-01", respuesta.fechaActualizacion)
    }

    @Test
    fun `dos respuestas iguales deben ser iguales`() {
        val r1 = RespuestaUsuario(
            1, "OK", 200, "1", "user", "Ana", "Gómez", "ana@mail.com", "3211231234",
            "rol1", "Rol A", "perfil1", "2024-01-01", "2024-05-01"
        )

        val r2 = RespuestaUsuario(
            1, "OK", 200, "1", "user", "Ana", "Gómez", "ana@mail.com", "3211231234",
            "rol1", "Rol A", "perfil1", "2024-01-01", "2024-05-01"
        )

        assertEquals(r1, r2)
    }

    @Test
    fun `respuestas diferentes no deben ser iguales`() {
        val r1 = RespuestaUsuario(1, "OK", 200, "1", "user", "Ana", "Gómez", "ana@mail.com", "321", "rol1", "Rol A", "perfil1", "2024-01-01", "2024-05-01")
        val r2 = RespuestaUsuario(0, "Error", 401, "2", "otro", "Luis", "Ruiz", "luis@mail.com", "123", "rol2", "Rol B", "perfil2", "2023-01-01", "2023-05-01")

        assertNotEquals(r1, r2)
    }
}
