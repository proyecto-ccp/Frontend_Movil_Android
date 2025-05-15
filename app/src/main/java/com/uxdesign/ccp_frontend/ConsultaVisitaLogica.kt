package com.uxdesign.ccp_frontend.logic

class ConsultaVisitaLogic {
    fun validarDatos(idUsuario: String?, fecha: String): ResultadoValidacion {
        return when {
            idUsuario.isNullOrEmpty() -> ResultadoValidacion.Error("ID de usuario no disponible")
            fecha.isEmpty() -> ResultadoValidacion.Error("Debes ingresar una fecha")
            else -> ResultadoValidacion.Exito
        }
    }

    sealed class ResultadoValidacion {
        object Exito : ResultadoValidacion()
        data class Error(val mensaje: String) : ResultadoValidacion()
    }
}
