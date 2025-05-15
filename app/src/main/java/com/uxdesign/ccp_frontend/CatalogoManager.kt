package com.uxdesign.ccp_frontend

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CatalogoManager(private val apiService: ApiService) {

    interface CatalogoCallback {
        fun onExito(productos: List<Producto>)
        fun onError(mensaje: String)
    }

    fun obtenerCatalogo(callback: CatalogoCallback) {
        apiService.getProductos().enqueue(object : Callback<RespuestaProducto> {
            override fun onResponse(
                call: Call<RespuestaProducto>,
                response: Response<RespuestaProducto>
            ) {
                if (response.isSuccessful) {
                    val productos = response.body()?.productos ?: emptyList()
                    callback.onExito(productos)
                } else {
                    callback.onError("Error al cargar el catálogo")
                }
            }

            override fun onFailure(call: Call<RespuestaProducto>, t: Throwable) {
                callback.onError("Error de conexión en catálogo")
            }
        })
    }
}
