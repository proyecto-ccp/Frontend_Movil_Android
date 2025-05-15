package com.uxdesign.ccp_frontend

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VideoDataManager(private val apiService: ApiService) {
    fun getVideosPorCliente(
        clienteId: String,
        onSuccess: (List<Video>) -> Unit,
        onEmpty: () -> Unit,
        onError: () -> Unit
    ) {
        apiService.getVideosPorCliente(clienteId).enqueue(object : Callback<RespuestaVideo> {
            override fun onResponse(call: Call<RespuestaVideo>, response: Response<RespuestaVideo>) {
                if (response.isSuccessful) {
                    val lista = response.body()?.videos ?: emptyList()
                    if (lista.isEmpty()) onEmpty() else onSuccess(lista)
                } else {
                    onEmpty()
                }
            }

            override fun onFailure(call: Call<RespuestaVideo>, t: Throwable) {
                t.printStackTrace()
                onError()
            }
        })
    }
}
