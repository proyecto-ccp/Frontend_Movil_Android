package com.uxdesign.ccp_frontend

import retrofit2.*

class VisitaDataManager(private val apiService: ApiService) {

    // Eliminamos la creación interna del apiService y lo inyectamos por el constructor

    fun cargarCiudadVisitas(
        fecha: String,
        idUsuario: String,
        onSuccess: (List<VisitaRequest>) -> Unit,
        onEmpty: () -> Unit,
        onError: () -> Unit
    ) {
        val fechaFormateada = "${fecha}T00:00:00.420Z"
        apiService.getVisitasPorFecha(fechaFormateada, idUsuario).enqueue(object : Callback<RespuestaVisita> {
            override fun onResponse(call: Call<RespuestaVisita>, response: Response<RespuestaVisita>) {
                if (response.isSuccessful) {
                    val lista = response.body()?.visitas ?: emptyList()
                    if (lista.isEmpty()) onEmpty() else onSuccess(lista)
                } else {
                    onEmpty()
                }
            }

            override fun onFailure(call: Call<RespuestaVisita>, t: Throwable) {
                t.printStackTrace()
                onError()
            }
        })
    }

    fun extraerCiudades(visitas: List<VisitaRequest>): List<CiudadVisita> {
        val agrupado = mutableMapOf<Pair<String, String>, Int>()
        visitas.forEach {
            val ciudad = it.cliente.ciudad
            val fecha = it.fechaVisita?.substring(0, 10) ?: "Sin fecha"
            val key = ciudad to fecha
            agrupado[key] = agrupado.getOrDefault(key, 0) + 1
        }

        return agrupado.map { (key, cantidad) ->
            CiudadVisita(key.first, cantidad, key.second)
        }
    }
}
