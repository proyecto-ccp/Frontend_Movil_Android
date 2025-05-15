package com.uxdesign.ccp_frontend

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PedidoRepository(private val apiService: ApiService) {

    fun obtenerDetallePedido(
        idUsuario: String,
        onSuccess: (List<ProductoCarrito>) -> Unit,
        onError: (String) -> Unit
    ) {
        apiService.getDetallePedidoUsuario(idUsuario).enqueue(object : Callback<RespuestaDetalleCarrito> {
            override fun onResponse(call: Call<RespuestaDetalleCarrito>, response: Response<RespuestaDetalleCarrito>) {
                if (response.isSuccessful) {
                    val productos = response.body()?.detallePedidos ?: emptyList()
                    onSuccess(productos)
                } else {
                    onError("No tienes productos en el carrito")
                }
            }

            override fun onFailure(call: Call<RespuestaDetalleCarrito>, t: Throwable) {
                onError("Error de conexión")
            }
        })
    }
}
