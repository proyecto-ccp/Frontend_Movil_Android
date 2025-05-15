package com.uxdesign.ccp_frontend

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsultasClientesManagerTest {

    @Mock
    lateinit var apiService: ApiService
    @Mock
    lateinit var call: Call<RespuestaCliente>

    private lateinit var consultasClientesManager: ConsultasClientesManager

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        consultasClientesManager = ConsultasClientesManager(apiService)
    }

    @Test
    fun testObtenerClientesPorZona_Success() {
        val clientesMock = listOf(
            Cliente("1", "Juan", "Perez", "12345678", "DNI", "987654321", "juan@email.com", "Calle 123", "Ciudad A", "11e86372-1b67-4d4b-b234-53f716dab601", "Zona 1"),
            Cliente("2", "Maria", "Lopez", "23456789", "DNI", "987654322", "maria@email.com", "Calle 456", "Ciudad B", "11e86372-1b67-4d4b-b234-53f716dab601", "Zona 2")
        )
        val respuestaMock = RespuestaCliente(1, "Éxito", 200, clientesMock)

        Mockito.`when`(apiService.getClientesPorZona(Mockito.anyString())).thenReturn(call)

        Mockito.doAnswer { invocation ->
            val callback = invocation.getArgument<Callback<RespuestaCliente>>(0)
            callback.onResponse(call, Response.success(respuestaMock))
            null
        }.`when`(call).enqueue(Mockito.any())

        consultasClientesManager.obtenerClientesPorZona("zona123", object : ConsultasClientesManager.ClientesCallback {
            override fun onExito(clientes: List<Cliente>) {
                assert(clientes.size == 2)
                assert(clientes[0].nombre == "Juan")
                assert(clientes[1].apellido == "Lopez")
            }

            override fun onError(mensaje: String) {
                assert(false) { "Debería haber sido un éxito." }
            }
        })
    }

    @Test
    fun testObtenerClientesPorZona_Error() {
        Mockito.`when`(apiService.getClientesPorZona(Mockito.anyString())).thenReturn(call)

        Mockito.doAnswer { invocation ->
            val callback = invocation.getArgument<Callback<RespuestaCliente>>(0)
            callback.onFailure(call, Throwable("Error de conexión"))
            null
        }.`when`(call).enqueue(Mockito.any())

        consultasClientesManager.obtenerClientesPorZona("zona123", object : ConsultasClientesManager.ClientesCallback {
            override fun onExito(clientes: List<Cliente>) {
                assert(false) { "Debería haber sido un error." }
            }

            override fun onError(mensaje: String) {
                assert(mensaje == "Error de conexión en consultar clientes")
            }
        })
    }
}
