package com.uxdesign.ccp_frontend

import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VisitaDataManagerTest {

    private lateinit var apiService: ApiService
    private lateinit var visitaDataManager: VisitaDataManager
    private lateinit var mockCall: Call<RespuestaVisita>

    @Before
    fun setup() {
        apiService = mock()
        mockCall = mock()
        visitaDataManager = VisitaDataManager(apiService)
    }

    @Test
    fun `cargarCiudadVisitas - respuesta exitosa con datos`() {
        val visitaList = listOf(
            VisitaRequest(
                id = 1,
                idCliente = "1",
                idVendedor = "2",
                cliente = Cliente(
                    id = "1",
                    nombre = "Juan",
                    apellido = "Pérez",
                    documento = "12345678",
                    tipoDocumento = "DNI",
                    telefono = "123456789",
                    email = "juan.perez@mail.com",
                    direccion = "Av. Siempre Viva 123",
                    ciudad = "Madrid",
                    idZona = "Z1",
                    zona = "Centro"
                ),
                fechaVisita = "2023-05-01T10:00:00.000Z",
                motivo = "Seguimiento",
                resultado = "Exitoso",
                estado = "Completado"
            )
        )

        val respuesta = RespuestaVisita(
            id = "1",
            resultado = 1,
            mensaje = "OK",
            status = 200,
            visitas = visitaList
        )

        whenever(apiService.getVisitasPorFecha(any(), any())).thenReturn(mockCall)

        doAnswer {
            val callback: Callback<RespuestaVisita> = it.getArgument(0)
            callback.onResponse(mockCall, Response.success(respuesta))
            null
        }.whenever(mockCall).enqueue(any())

        var resultList: List<VisitaRequest>? = null
        var wasEmpty = false
        var wasError = false

        visitaDataManager.cargarCiudadVisitas(
            fecha = "2023-05-01",
            idUsuario = "1",
            onSuccess = { resultList = it },
            onEmpty = { wasEmpty = true },
            onError = { wasError = true }
        )

        assert(resultList != null && resultList!!.isNotEmpty())
        assert(!wasEmpty)
        assert(!wasError)
    }

    @Test
    fun `cargarCiudadVisitas - respuesta vacía`() {
        val respuesta = RespuestaVisita(
            id = "1",
            resultado = 0,
            mensaje = "Sin datos",
            status = 200,
            visitas = emptyList()
        )

        whenever(apiService.getVisitasPorFecha(any(), any())).thenReturn(mockCall)

        doAnswer {
            val callback: Callback<RespuestaVisita> = it.getArgument(0)
            callback.onResponse(mockCall, Response.success(respuesta))
            null
        }.whenever(mockCall).enqueue(any())

        var wasEmpty = false

        visitaDataManager.cargarCiudadVisitas(
            fecha = "2023-05-01",
            idUsuario = "1",
            onSuccess = { },
            onEmpty = { wasEmpty = true },
            onError = { }
        )

        assert(wasEmpty)
    }

    @Test
    fun `extraerCiudades agrupa correctamente por ciudad y fecha`() {
        val visitas = listOf(
            VisitaRequest(
                id = 1,
                idCliente = "1",
                idVendedor = "2",
                cliente = Cliente(
                    id = "1",
                    nombre = "Juan",
                    apellido = "Pérez",
                    documento = "12345678",
                    tipoDocumento = "DNI",
                    telefono = "123456789",
                    email = "juan@mail.com",
                    direccion = "Calle Falsa 123",
                    ciudad = "Madrid",
                    idZona = "Z1",
                    zona = "Centro"
                ),
                fechaVisita = "2023-05-01T10:00:00.000Z",
                motivo = "Reunión",
                resultado = "Completado",
                estado = "Activo"
            ),
            VisitaRequest(
                id = 2,
                idCliente = "2",
                idVendedor = "2",
                cliente = Cliente(
                    id = "2",
                    nombre = "Ana",
                    apellido = "López",
                    documento = "87654321",
                    tipoDocumento = "DNI",
                    telefono = "987654321",
                    email = "ana@mail.com",
                    direccion = "Calle Verdadera 456",
                    ciudad = "Madrid",
                    idZona = "Z1",
                    zona = "Centro"
                ),
                fechaVisita = "2023-05-01T11:00:00.000Z",
                motivo = "Cobranza",
                resultado = "Pendiente",
                estado = "Programado"
            )
        )

        val resultado = visitaDataManager.extraerCiudades(visitas)

        assert(resultado.size == 1)
        assert(resultado[0].ciudad == "Madrid")
        assert(resultado[0].cantidad == 2)
        assert(resultado[0].fecha == "2023-05-01")
    }
}
