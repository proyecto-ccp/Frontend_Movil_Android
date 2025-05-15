package com.uxdesign.ccp_frontend

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductoManager(private val apiService: ApiService) {

    interface StockCallback {
        fun onStockRecibido(stock: Int)
        fun onError(mensaje: String)
    }

    fun obtenerStock(productoId: Int, callback: StockCallback) {
        apiService.getStockProducto(productoId).enqueue(object : Callback<RespuestaInventario> {
            override fun onResponse(
                call: Call<RespuestaInventario>,
                response: Response<RespuestaInventario>
            ) {
                if (response.isSuccessful) {
                    val stock = response.body()?.inventario?.cantidadStock ?: 0
                    callback.onStockRecibido(stock)
                } else {
                    callback.onError("No se pudo obtener el stock")
                }
            }

            override fun onFailure(call: Call<RespuestaInventario>, t: Throwable) {
                callback.onError("Error de conexión al obtener stock")
            }
        })
    }

    interface AgregarProductoCallback {
        fun onAgregado()
        fun onError(mensaje: String)
    }

    fun agregarProducto(producto: ProductoCarrito, callback: AgregarProductoCallback) {
        apiService.agregarDetallePedido(producto).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    callback.onAgregado()
                } else {
                    callback.onError("Error al agregar producto")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                callback.onError("Error de red al agregar producto")
            }
        })
    }
}
