import com.uxdesign.ccp_frontend.*
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.junit.Assert.*

class PedidoRepositoryTest {

    private lateinit var apiService: ApiService
    private lateinit var pedidoRepository: PedidoRepository
    private lateinit var mockCall: Call<RespuestaDetalleCarrito>

    @Before
    fun setUp() {
        apiService = mock()
        mockCall = mock()
        pedidoRepository = PedidoRepository(apiService)
    }

    @Test
    fun `debe retornar productos cuando la respuesta es exitosa`() {
        val productosMock = listOf(
            ProductoCarrito(idProducto = 1, cantidad = 2, idUsuario = "123", precioUnitario = 10.0),
            ProductoCarrito(idProducto = 2, cantidad = 1, idUsuario = "123", precioUnitario = 20.0)
        )

        val respuesta = RespuestaDetalleCarrito(
            resultado = 1,
            mensaje = "Consulta exitosa",
            status = 200,
            detallePedidos = productosMock
        )

        val response = Response.success(respuesta)
        val callbackCaptor = argumentCaptor<Callback<RespuestaDetalleCarrito>>()

        whenever(apiService.getDetallePedidoUsuario(any())).thenReturn(mockCall)

        var resultado: List<ProductoCarrito>? = null
        var error: String? = null

        pedidoRepository.obtenerDetallePedido("123", {
            resultado = it
        }, {
            error = it
        })

        verify(mockCall).enqueue(callbackCaptor.capture())
        callbackCaptor.firstValue.onResponse(mockCall, response)

        assertNotNull(resultado)
        assertEquals(2, resultado!!.size)
        assertNull(error)
    }
}
