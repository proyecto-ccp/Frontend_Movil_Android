package com.uxdesign.ccp_frontend

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsultasClientesManager(private val apiService: ApiService) {

    interface ClientesCallback {
        fun onExito(clientes: List<Cliente>)
        fun onError(mensaje: String)
    }

    fun obtenerClientesPorZona(idZona: String, callback: ClientesCallback) {
        apiService.getClientesPorZona(idZona).enqueue(object : Callback<RespuestaCliente> {
            override fun onResponse(call: Call<RespuestaCliente>, response: Response<RespuestaCliente>) {
                if (response.isSuccessful) {
                    val clientes = response.body()?.clientes ?: emptyList()
                    callback.onExito(clientes)
                } else {
                    callback.onError("Error al cargar los clientes")
                }
            }

            override fun onFailure(call: Call<RespuestaCliente>, t: Throwable) {
                callback.onError("Error de conexión en consultar clientes")
            }
        })
    }
}
