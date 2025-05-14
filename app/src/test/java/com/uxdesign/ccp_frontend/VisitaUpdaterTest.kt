import com.uxdesign.ccp_frontend.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.kotlin.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse

class VisitaUpdaterTest {

    private lateinit var apiService: ApiService
    private lateinit var visitaUpdater: VisitaUpdater
    private lateinit var mockCall: Call<RespuestaRequest>

    @Before
    fun setUp() {
        apiService = mock()
        mockCall = mock()
        visitaUpdater = VisitaUpdater(apiService)
    }

    @Test
    fun `debe llamar onSuccess si la API responde con éxito usando enqueue`() {

        val cliente = Cliente(
            id = "1", nombre = "Juan", apellido = "Pérez",
            documento = "12345678", tipoDocumento = "DNI", telefono = "555-1234",
            email = "juan@example.com", direccion = "Calle 123",
            ciudad = "Bogotá", idZona = "Z1", zona = "Norte"
        )

        val visita = VisitaRequest(
            id = 1, idCliente = cliente.id, idVendedor = "vendedor1",
            cliente = cliente, fechaVisita = "2025-05-01",
            motivo = "Demo", resultado = "OK", estado = "REALIZADA"
        )

        val respuesta = RespuestaRequest("1", 1, "Actualizado", 200)
        val response = Response.success(respuesta)

        val callbackCaptor = argumentCaptor<Callback<RespuestaRequest>>()

        whenever(apiService.modificarEstadoVisita(any(), any())).thenReturn(mockCall)

        var successCalled = false
        var errorCalled = false

        visitaUpdater.actualizarVisita(visita, {
            successCalled = true
        }, {
            errorCalled = true
        })

        verify(mockCall).enqueue(callbackCaptor.capture())
        callbackCaptor.firstValue.onResponse(mockCall, response)

        assertTrue(successCalled)
        assertFalse(errorCalled)
    }
}
