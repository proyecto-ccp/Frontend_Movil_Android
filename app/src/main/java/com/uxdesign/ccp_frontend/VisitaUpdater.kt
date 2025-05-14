package com.uxdesign.ccp_frontend

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VisitaUpdater(private val apiService: ApiService) {

    fun actualizarVisita(visita: VisitaRequest, onSuccess: () -> Unit, onError: (String) -> Unit) {
        apiService.modificarEstadoVisita(visita.id, visita)
            .enqueue(object : Callback<RespuestaRequest> {
                override fun onResponse(call: Call<RespuestaRequest>, response: Response<RespuestaRequest>) {
                    if (response.isSuccessful) {
                        onSuccess()
                    } else {
                        onError("No fue posible actualizar la visita")
                    }
                }

                override fun onFailure(call: Call<RespuestaRequest>, t: Throwable) {
                    onError("Error de conexión con visitas")
                }
            })
    }
}
