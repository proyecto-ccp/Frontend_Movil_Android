package com.uxdesign.ccp_frontend

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("Video/CargarVideo")
    fun uploadVideo(@Body videoRequest: VideoRequest): Call<ResponseBody>

    @GET("Video/ObtenerVideosPorCliente/{clienteId}")
    fun getVideosPorCliente(@Path("clienteId") clienteId: String): Call<RespuestaVideo>

    @GET("Proveedores/Listar")
    fun getProveedores(): Call<RespuestaProveedor>

    @GET("Productos/ConsultarPorProveedor")
    fun getProductosPorProveedor(@Query("idProveedor") idProveedor: String): Call<RespuestaProducto>

    @GET("Productos/Consultar")
    fun getProductos(): Call<RespuestaProducto>

    @GET("Cliente/ObtenerClientesPorZona/{zonaId}")
    fun getClientesPorZona(@Path("zonaId") zonaId: String): Call<RespuestaCliente>


    @POST("DetallePedido/AgregarDetalle")
    fun agregarDetallePedido(@Body detalle: ProductoCarrito): Call<Void>

    @GET("Inventarios/Consultar")
    fun getStockProducto(@Query("idProducto") idProducto: Int): Call<RespuestaInventario>

    @GET("DetallePedido/ObtenerDetallesUsuario/{id}")
    fun getDetallePedidoUsuario(@Path("id") clienteId: String): Call<RespuestaDetalleCarrito>

    @GET("Vendedor/{idVendedor}")
    fun getVendedor(@Path("idVendedor") vendedorId: String): Call<RespuestaVendedor>

    @POST("Pedido/CrearPedido")
    fun crearPedido(@Body request: Pedido): Call<RespuestaRequest>

    @PUT("DetallePedido/ActualizarDetalles/{idUsuario}/{idPedido}")
    fun enlazarDetallePedido(@Path("idUsuario") idUsuario: String,
        @Path("idPedido") idPedido: String): Call<RespuestaRequest>

    @POST("Visita/CrearVisita")
    fun crearVisita(@Body request: Visita): Call<RespuestaRequest>

    @GET("Visita/ObtenerVisitasPorFecha/{fecha}/{vendedorId}")
    fun getVisitasPorFecha(@Path("fecha") fecha: String, @Path("vendedorId") vendedorId: String
    ): Call<RespuestaVisita>

    @PUT("Visita/ModificarVisita/{id}")
    fun modificarEstadoVisita(@Path("id") idVisita: Int, @Body request: VisitaRequest): Call<RespuestaRequest>
}

