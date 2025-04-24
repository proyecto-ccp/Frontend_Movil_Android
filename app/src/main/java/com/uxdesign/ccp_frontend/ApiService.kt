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
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("CargarVideo")  // Reemplaza con la URL de tu microservicio
    fun uploadVideo(@Body videoRequest: VideoRequest): Call<ResponseBody>

    @GET("Video/ObtenerVideosPorCliente/{clienteId}")
    fun getVideosPorCliente(@Path("clienteId") clienteId: String): Call<RespuestaVideo>

    @POST("Cliente/CrearCliente") // <- Ajusta el endpoint
    fun registrarCliente(@Body cliente: Cliente): Call<ResponseBody>

    @GET("Proveedores/Listar")
    fun getProveedores(): Call<RespuestaProveedor>

    @GET("Productos/ConsultarPorProveedor")
    fun getProductosPorProveedor(@Query("idProveedor") idProveedor: String): Call<RespuestaProducto>

    @GET("Productos/Consultar")
    fun getProductos(): Call<List<Producto>>

    @GET("Ciudad")
    fun getCiudades(): Call<List<Ciudad>>

    @GET("Zona")
    fun getZonasPorCiudad(@Path("ciudadId") ciudadId: String): Call<List<Zona>>

    @GET("Cliente/ObtenerClientesPorZona/{zonaId}")
    fun getClientesPorZona(@Path("zonaId") zonaId: String): Call<RespuestaCliente>

    @GET("Pedidos/ConsultarPorCliente/{id}")
    fun getPedidosPorCliente(@Path("id") clienteId: String): Call<List<Pedido>>

    @POST("carrito/agregar")
    fun agregarProducto(@Body producto: ProductoCarrito): Call<Void>

}
