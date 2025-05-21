package com.uxdesign.ccp_frontend

import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StockManagerTest {

    private lateinit var apiService: ApiService
    private lateinit var mockCall: Call<RespuestaInventario>
    private lateinit var stockManager: StockManager

    @Before
    fun setUp() {
        apiService = mock()
        mockCall = mock()
        stockManager = StockManager(apiService)
    }

    @Test
    fun `obtenerStock con respuesta exitosa retorna el stock correcto`() {
        val productoId = 123
        val inventario = Inventario(
            id = "inv001",
            idProducto = productoId,
            cantidadStock = 25,
            fechaCreacion = "2023-01-01T00:00:00Z",
            fechaModificacion = "2023-05-01T00:00:00Z"
        )
        val respuestaInventario = RespuestaInventario(
            resultado = 1,
            mensaje = "OK",
            status = 200,
            inventario = inventario
        )

        whenever(apiService.getStockProducto(productoId)).thenReturn(mockCall)

        doAnswer {
            val callback: Callback<RespuestaInventario> = it.getArgument(0)
            callback.onResponse(mockCall, Response.success(respuestaInventario))
        }.whenever(mockCall).enqueue(any())

        val callback = object : StockManager.StockCallback {
            override fun onStockRecibido(stock: Int) {
                assert(stock == 25)
            }

            override fun onError(mensaje: String) {
                assert(false) { "No se esperaba error: $mensaje" }
            }
        }

        stockManager.obtenerStock(productoId, callback)
    }

    @Test
    fun `obtenerStock con respuesta fallida llama a onError`() {
        val productoId = 123

        whenever(apiService.getStockProducto(productoId)).thenReturn(mockCall)

        doAnswer {
            val callback: Callback<RespuestaInventario> = it.getArgument(0)
            callback.onResponse(mockCall, Response.error(404, okhttp3.ResponseBody.create(null, "Not Found")))
        }.whenever(mockCall).enqueue(any())

        val callback = object : StockManager.StockCallback {
            override fun onStockRecibido(stock: Int) {
                assert(false) { "No debería haberse recibido stock" }
            }

            override fun onError(mensaje: String) {
                assert(mensaje == "No se pudo obtener el stock")
            }
        }

        stockManager.obtenerStock(productoId, callback)
    }

    @Test
    fun `obtenerStock con fallo de red llama a onError`() {
        val productoId = 123

        whenever(apiService.getStockProducto(productoId)).thenReturn(mockCall)

        doAnswer {
            val callback: Callback<RespuestaInventario> = it.getArgument(0)
            callback.onFailure(mockCall, Throwable("Fallo de red"))
        }.whenever(mockCall).enqueue(any())

        val callback = object : StockManager.StockCallback {
            override fun onStockRecibido(stock: Int) {
                assert(false) { "No debería haberse recibido stock" }
            }

            override fun onError(mensaje: String) {
                assert(mensaje == "Error de conexión al obtener stock")
            }
        }

        stockManager.obtenerStock(productoId, callback)
    }
}
