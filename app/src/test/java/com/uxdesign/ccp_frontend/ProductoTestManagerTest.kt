package com.uxdesign.ccp_frontend

import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductoTestManagerTest {

    private lateinit var apiService: ApiService
    private lateinit var callVoid: Call<Void>
    private lateinit var productoManager: ProductoManager

    @Before
    fun setUp() {
        // Mocking del ApiService y Call
        apiService = mock()
        callVoid = mock()
        productoManager = ProductoManager(apiService)
    }

    @Test
    fun `agregarProducto con respuesta exitosa llama a onAgregado`() {
        val producto = ProductoCarrito(
            idProducto = 1,
            cantidad = 2,
            idUsuario = "user123",
            precioUnitario = 99.99
        )

        // Simulamos la llamada a agregarDetallePedido y que devuelva un Call<Void>
        whenever(apiService.agregarDetallePedido(producto)).thenReturn(callVoid)

        // Simulamos la respuesta exitosa
        doAnswer {
            val callback: Callback<Void> = it.getArgument(0)
            callback.onResponse(callVoid, Response.success(null))
        }.whenever(callVoid).enqueue(any())

        // Callback para verificar que se llame a onAgregado
        val callback = object : ProductoManager.AgregarProductoCallback {
            override fun onAgregado() {
                assert(true) // Confirmamos que se llama correctamente
            }

            override fun onError(mensaje: String) {
                assert(false) { "No se esperaba error: $mensaje" }
            }
        }

        // Llamamos al método y verificamos si onAgregado es invocado
        productoManager.agregarProducto(producto, callback)
    }

    @Test
    fun `agregarProducto con error de servidor llama a onError`() {
        val producto = ProductoCarrito(
            idProducto = 2,
            cantidad = 1,
            idUsuario = "user456",
            precioUnitario = 150.0
        )

        // Simulamos la llamada a agregarDetallePedido y que devuelva un Call<Void>
        whenever(apiService.agregarDetallePedido(producto)).thenReturn(callVoid)

        // Simulamos una respuesta de error
        doAnswer {
            val callback: Callback<Void> = it.getArgument(0)
            callback.onResponse(callVoid, Response.error(400, okhttp3.ResponseBody.create(null, "Error")))
        }.whenever(callVoid).enqueue(any())

        // Callback para verificar que se llame a onError
        val callback = object : ProductoManager.AgregarProductoCallback {
            override fun onAgregado() {
                assert(false) { "No debería llamarse onAgregado" }
            }

            override fun onError(mensaje: String) {
                assert(mensaje == "Error al agregar producto")
            }
        }

        // Llamamos al método y verificamos si onError es invocado con el mensaje esperado
        productoManager.agregarProducto(producto, callback)
    }

    @Test
    fun `agregarProducto con fallo de red llama a onError con mensaje de red`() {
        val producto = ProductoCarrito(
            idProducto = 3,
            cantidad = 5,
            idUsuario = "user789",
            precioUnitario = 49.95
        )

        // Simulamos la llamada a agregarDetallePedido y que devuelva un Call<Void>
        whenever(apiService.agregarDetallePedido(producto)).thenReturn(callVoid)

        // Simulamos una falla de red
        doAnswer {
            val callback: Callback<Void> = it.getArgument(0)
            callback.onFailure(callVoid, Throwable("Network error"))
        }.whenever(callVoid).enqueue(any())

        // Callback para verificar que se llame a onError con el mensaje correcto
        val callback = object : ProductoManager.AgregarProductoCallback {
            override fun onAgregado() {
                assert(false) { "No debería llamarse onAgregado" }
            }

            override fun onError(mensaje: String) {
                assert(mensaje == "Error de red al agregar producto")
            }
        }

        // Llamamos al método y verificamos si onError es invocado con el mensaje de error de red
        productoManager.agregarProducto(producto, callback)
    }
}
