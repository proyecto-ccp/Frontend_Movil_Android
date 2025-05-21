package com.uxdesign.ccp_frontend

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductoManager(private val apiService: ApiService) {

    interface StockCallback {
        fun onStockRecibido(stock: Int)
        fun onError(mensaje: String)
    }

   interface AgregarProductoCallback {
        fun onAgregado()
        fun onError(mensaje: String)
    }

    fun agregarProducto(producto: ProductoCarrito, callback: AgregarProductoCallback) {
        Log.d("ProductoManager", "Agregando producto al carrito: $producto") // Log para el producto que se agrega

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