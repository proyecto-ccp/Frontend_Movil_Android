package com.uxdesign.ccp_frontend

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.ArgumentCaptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatalogoManagerTest {

    private val apiService = mock(ApiService::class.java)
    private val call = mock(Call::class.java) as Call<RespuestaProducto>
    private val catalogoManager = CatalogoManager(apiService)

    @Test
    fun `obtenerCatalogo llama onExito cuando la respuesta es exitosa`() {
        val productosMock = listOf(
            Producto(
                id = 1,
                nombre = "Producto A",
                descripcion = "Descripción del producto A",
                idProveedor = "Proveedor A",
                precioUnitario = 10.0,
                cantidad = 100,
                idMedida = 1,
                idCategoria = 1,
                idMarca = 1,
                idColor = 1,
                idModelo = 1,
                idMaterial = 1,
                urlFoto1 = "http://example.com/imagen1.jpg",
                urlFoto2 = "http://example.com/imagen2.jpg"
            )
        )
        val respuestaMock = RespuestaProducto(
            resultado = 1,
            mensaje = "OK",
            status = 200,
            productos = productosMock
        )

        `when`(apiService.getProductos()).thenReturn(call)

        val callbackCaptor = ArgumentCaptor.forClass(Callback::class.java) as ArgumentCaptor<Callback<RespuestaProducto>>

        val mockCallback = object : CatalogoManager.CatalogoCallback {
            override fun onExito(productos: List<Producto>) {
                assert(productos == productosMock)
                assert(productos[0].nombre == "Producto A")
                assert(productos[0].precioUnitario == 10.0)
            }

            override fun onError(mensaje: String) {
                assert(false) { "No debería llamarse onError" }
            }
        }

        catalogoManager.obtenerCatalogo(mockCallback)
        verify(call).enqueue(callbackCaptor.capture())
        callbackCaptor.value.onResponse(call, Response.success(respuestaMock))
    }

    @Test
    fun `obtenerCatalogo llama onError cuando la respuesta es fallida`() {
        `when`(apiService.getProductos()).thenReturn(call)

        val callbackCaptor = ArgumentCaptor.forClass(Callback::class.java) as ArgumentCaptor<Callback<RespuestaProducto>>

        val mockCallback = object : CatalogoManager.CatalogoCallback {
            override fun onExito(productos: List<Producto>) {
                assert(false) { "No debería llamarse onExito" }
            }

            override fun onError(mensaje: String) {
                assert(mensaje == "Error al cargar el catálogo")
            }
        }

        catalogoManager.obtenerCatalogo(mockCallback)

        verify(call).enqueue(callbackCaptor.capture())

        val errorBody = ResponseBody.create("application/json".toMediaTypeOrNull(), "Error")
        callbackCaptor.value.onResponse(call, Response.error(500, errorBody))
    }

    @Test
    fun `obtenerCatalogo llama onError cuando ocurre una excepción de red`() {
        `when`(apiService.getProductos()).thenReturn(call)

        val callbackCaptor = ArgumentCaptor.forClass(Callback::class.java) as ArgumentCaptor<Callback<RespuestaProducto>>

        val mockCallback = object : CatalogoManager.CatalogoCallback {
            override fun onExito(productos: List<Producto>) {
                assert(false) { "No debería llamarse onExito" }
            }

            override fun onError(mensaje: String) {
                assert(mensaje == "Error de conexión en catálogo")
            }
        }

        catalogoManager.obtenerCatalogo(mockCallback)
        verify(call).enqueue(callbackCaptor.capture())
        callbackCaptor.value.onFailure(call, Throwable("Falla de red"))
    }
}
