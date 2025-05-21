package com.uxdesign.ccp_frontend

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ConsultaVisitaLogica {
    fun validarDatos(idUsuario: String?, fecha: String): ResultadoValidacion {
        return when {
            idUsuario.isNullOrEmpty() -> ResultadoValidacion.Error("ID de usuario no disponible")
            fecha.isEmpty() -> ResultadoValidacion.Error("Debes ingresar una fecha")
            !esFechaValidaYNoAnterior(fecha) -> ResultadoValidacion.Error("La fecha no puede ser anterior a hoy")
            else -> ResultadoValidacion.Exito
        }
    }

    sealed class ResultadoValidacion {
        object Exito : ResultadoValidacion()
        data class Error(val mensaje: String) : ResultadoValidacion()
    }

    private fun esFechaValidaYNoAnterior(fecha: String): Boolean {
        return try {
            val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            formato.isLenient = false
            val fechaIngresada = formato.parse(fecha)

            val hoy = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time

            !fechaIngresada.before(hoy)
        } catch (e: Exception) {
            false
        }
    }
}
