package com.uxdesign.ccp_frontend

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("Video/CargarVideo")
    fun uploadVideo(@Body videoRequest: VideoRequest): Call<ResponseBody>

    @GET("Video/ObtenerVideosPorCliente/{clienteId}")
    fun getVideosPorCliente(@Path("clienteId") clienteId: String): Call<RespuestaVideo>

    @POST("Cliente/CrearCliente")
    fun registrarCliente(@Body cliente: Cliente): Call<ResponseBody>

    @GET("Proveedores/Listar")
    fun getProveedores(): Call<RespuestaProveedor>

    @GET("Atributos/TiposDocumento")
    fun getTiposDocumento(): Call<RespuestaTiposDocs>

    @GET("Productos/ConsultarPorProveedor")
    fun getProductosPorProveedor(@Query("idProveedor") idProveedor: String): Call<RespuestaProducto>

    @GET("Productos/Consultar")
    fun getProductos(): Call<RespuestaProducto>

    @GET("atributos/Localizacion/Ciudades")
    fun getCiudades(): Call<RespuestaCiudad>

    @GET("Atributos/Localizacion/Ciudad/{IdCiudad}/Zonas")
    fun getZonasPorCiudad(@Path("IdCiudad") idCiudad: String): Call<RespuestaZona>

    @GET("Cliente/ObtenerClientesPorZona/{zonaId}")
    fun getClientesPorZona(@Path("zonaId") zonaId: String): Call<RespuestaCliente>

    @GET("Pedido/ObtenerPedidosPorCliente/{clienteId}/{estado}")
    fun getPedidosPorCliente(@Path("clienteId") clienteId: String, @Path("estado") estado: String
    ): Call<RespuestaPedidoProcesado>
    
    @GET("Pedido/ObtenerPedidosPorVendedor/{vendedorId}/{estado}")
    fun getPedidosPorVendedor(@Path("vendedorId") vendedorId: String, @Path("estado") estado: String
    ): Call<RespuestaPedido>

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
}
