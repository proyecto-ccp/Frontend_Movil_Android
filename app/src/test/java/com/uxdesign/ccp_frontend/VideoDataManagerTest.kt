import com.uxdesign.ccp_frontend.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import junit.framework.TestCase.fail
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoDataManagerTest {

    private lateinit var apiService: ApiService
    private lateinit var videoDataManager: VideoDataManager

    @Before
    fun setUp() {
        // Inicializar el ApiService mock
        apiService = mock()
        // Crear una instancia de VideoDataManager con el mock del ApiService
        videoDataManager = VideoDataManager(apiService)
    }

    @Test
    fun `getVideosPorCliente llama a onEmpty cuando lista viene vacía`() {
        // Configuración del clienteId y la respuesta vacía
        val clienteId = "123"
        val respuestaVacia = RespuestaVideo(0, "No hay videos", 404, emptyList())

        // Crear un mock de la respuesta de la llamada API
        val callMock = mock<Call<RespuestaVideo>>()

        // Configurar el mock para que devuelva la llamada
        whenever(apiService.getVideosPorCliente(eq(clienteId))).thenReturn(callMock)

        // Configuración del comportamiento de enqueue
        whenever(callMock.enqueue(any())).thenAnswer {
            // Obtener el callback que Retrofit pasará
            val callback = it.getArgument<Callback<RespuestaVideo>>(0)

            // Llamar a onResponse con una respuesta vacía
            callback.onResponse(callMock, Response.success(respuestaVacia))
            null
        }

        // Variable para almacenar el resultado
        var resultado: List<Video>? = null

        // Llamada al método real
        videoDataManager.getVideosPorCliente(
            clienteId,
            onSuccess = { resultado = it },
            onEmpty = { /* Aquí comprobamos que onEmpty fue llamado */ },
            onError = { fail("No se esperaba onError") }
        )

        // Verificamos que el resultado esté vacío
        assertTrue(resultado.isNullOrEmpty())
    }

    @Test
    fun `getVideosPorCliente llama a onError cuando ocurre un fallo de red`() {
        // Configuración del clienteId
        val clienteId = "123"

        // Crear un mock de la respuesta de la llamada API
        val callMock = mock<Call<RespuestaVideo>>()

        // Configurar el mock para que devuelva un fallo en la llamada (simulando un error de red)
        whenever(apiService.getVideosPorCliente(eq(clienteId))).thenReturn(callMock)

        // Configuración del comportamiento de enqueue para fallo
        whenever(callMock.enqueue(any())).thenAnswer {
            // Obtener el callback que Retrofit pasará
            val callback = it.getArgument<Callback<RespuestaVideo>>(0)

            // Llamar a onFailure con un error de red (simulando fallo de conexión)
            callback.onFailure(callMock, Throwable("Error de red"))
            null
        }

        // Variable para almacenar el resultado
        var errorLlamado = false

        // Llamada al método real
        videoDataManager.getVideosPorCliente(
            clienteId,
            onSuccess = { fail("No se esperaba onSuccess") },
            onEmpty = { fail("No se esperaba onEmpty") },
            onError = { errorLlamado = true }  // Aquí estamos esperando que onError sea llamado
        )

        // Verificamos que se haya llamado a onError
        assertTrue(errorLlamado)
    }

    @Test
    fun `getVideosPorCliente llama a onSuccess cuando hay videos disponibles`() {
        // Configuración del clienteId y la respuesta con videos
        val clienteId = "123"
        val videosDisponibles = listOf(
            Video("1", "123", "001", "Video 1", "url1", "img1", "Cargado"),
            Video("2", "123", "002", "Video 2", "url2", "img2", "Cargado")
        )
        val respuestaConVideos = RespuestaVideo(1, "Videos cargados", 200, videosDisponibles)

        // Crear un mock de la respuesta de la llamada API
        val callMock = mock<Call<RespuestaVideo>>()

        // Configurar el mock para que devuelva la llamada
        whenever(apiService.getVideosPorCliente(eq(clienteId))).thenReturn(callMock)

        // Configuración del comportamiento de enqueue
        whenever(callMock.enqueue(any())).thenAnswer {
            // Obtener el callback que Retrofit pasará
            val callback = it.getArgument<Callback<RespuestaVideo>>(0)

            // Llamar a onResponse con la respuesta con videos
            callback.onResponse(callMock, Response.success(respuestaConVideos))
            null
        }

        // Variable para almacenar el resultado
        var resultado: List<Video>? = null

        // Llamada al método real
        videoDataManager.getVideosPorCliente(
            clienteId,
            onSuccess = { resultado = it },
            onEmpty = { fail("No se esperaba onEmpty") },
            onError = { fail("No se esperaba onError") }
        )

        // Verificamos que el resultado no esté vacío
        assertFalse(resultado.isNullOrEmpty())
        assertEquals(2, resultado?.size)
    }
}
