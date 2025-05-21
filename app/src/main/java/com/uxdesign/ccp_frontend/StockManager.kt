package com.uxdesign.ccp_frontend

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StockManager(private val apiServiceStock: ApiService) {

    interface StockCallback {
        fun onStockRecibido(stock: Int)
        fun onError(mensaje: String)
    }

    fun obtenerStock(productoId: Int, callback: StockCallback) {
        apiServiceStock.getStockProducto(productoId).enqueue(object : Callback<RespuestaInventario> {
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
}
