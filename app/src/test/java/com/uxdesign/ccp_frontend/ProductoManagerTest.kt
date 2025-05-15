package com.uxdesign.ccp_frontend

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.kotlin.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class ProductoManagerTest {

    private val apiService = mock<ApiService>()
    private val productoManager = ProductoManager(apiService)

    @Test
    fun `obtenerStock debe retornar stock correctamente`() {
        val productoId = 1
        val mockCall: Call<RespuestaInventario> = mock()
        val callbackCaptor = argumentCaptor<Callback<RespuestaInventario>>()

        whenever(apiService.getStockProducto(productoId)).thenReturn(mockCall)

        productoManager.obtenerStock(productoId, object : ProductoManager.StockCallback {
            override fun onStockRecibido(stock: Int) {
                assert(stock == 15)
            }

            override fun onError(mensaje: String) {
                assert(false)
            }
        })

        verify(mockCall).enqueue(callbackCaptor.capture())

        val respuesta = RespuestaInventario(
            resultado = 1,
            mensaje = "OK",
            status = 200,
            inventario = Inventario("1", productoId, 15, "fecha", "fecha")
        )
        val response = Response.success(respuesta)

        callbackCaptor.firstValue.onResponse(mockCall, response)
    }

    @Test
    fun `agregarProducto debe llamar onAgregado si exitoso`() {
        val producto = ProductoCarrito(1, 2, "usuario", 10.0)
        val mockCall: Call<Void> = mock()
        val callbackCaptor = argumentCaptor<Callback<Void>>()

        whenever(apiService.agregarDetallePedido(producto)).thenReturn(mockCall)

        productoManager.agregarProducto(producto, object : ProductoManager.AgregarProductoCallback {
            override fun onAgregado() {
                assert(true)
            }

            override fun onError(mensaje: String) {
                assert(false)
            }
        })

        verify(mockCall).enqueue(callbackCaptor.capture())

        val response = Response.success<Void>(null)

        callbackCaptor.firstValue.onResponse(mockCall, response)
    }
}
