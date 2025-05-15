package com.uxdesign.ccp_frontend

import retrofit2.Callback
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class PedidoLogic(private val apiService: ApiService) {

    fun obtenerVendedor(idUsuario: String, callback: Callback<RespuestaVendedor>) {
        apiService.getVendedor(idUsuario).enqueue(callback)
    }

    fun obtenerClientesPorZona(idZona: String, callback: Callback<RespuestaCliente>) {
        apiService.getClientesPorZona(idZona).enqueue(callback)
    }

    fun crearPedido(pedido: Pedido, callback: Callback<RespuestaRequest>) {
        apiService.crearPedido(pedido).enqueue(callback)
    }

    fun asociarDetalles(idUsuario: String, idPedido: String, callback: Callback<RespuestaRequest>) {
        apiService.enlazarDetallePedido(idUsuario, idPedido).enqueue(callback)
    }


    fun validarCampos(
        fecha: String,
        numProductos: String,
        total: String,
        clienteSeleccionado: Int
    ): Boolean {
        if (clienteSeleccionado <= 0) return false
        if (fecha.trim().isEmpty()) return false
        if (!validarFecha(fecha)) return false
        if (numProductos.trim().isEmpty()) return false
        if (total.trim().isEmpty()) return false
        return true
    }

    private fun validarFecha(fecha: String): Boolean {
        val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        formato.isLenient = false
        return try {
            val date = formato.parse(fecha)
            fecha == formato.format(date)
        } catch (e: ParseException) {
            false
        }
    }


    fun convertirFechaAISO8601(fecha: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        outputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date: Date = inputFormat.parse(fecha)!!
        return outputFormat.format(date)
    }
}
