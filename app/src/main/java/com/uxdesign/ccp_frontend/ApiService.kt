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

interface ApiService {

    //@Multipart
    //@POST("https://servicio-video-596275467600.us-central1.run.app/api/Video/CargarVideo/")
    //fun uploadVideo(
     //   @Part video: MultipartBody.Part,
      //  @Part("data") data: RequestBody
    //): Call<ResponseBody>
    @POST("CargarVideo")  // Reemplaza con la URL de tu microservicio
    fun uploadVideo(@Body videoRequest: VideoRequest): Call<ResponseBody>

    @GET("Video/ObtenerVideosPorCliente/{clienteId}")
    fun getVideosPorCliente(@Path("clienteId") clienteId: String): Call<List<Video>>

    @POST("cliente") // <- Ajusta el endpoint
    fun registrarCliente(@Body cliente: Cliente): Call<ResponseBody>

    @GET("Cliente")
    fun getClientes(): Call<List<Cliente>>

    @GET("Proveedor")
    fun getProveedores(): Call<List<Proveedor>>

    @GET("Productos/ConsultarPorProveedor/{id}")
    fun getProductosPorProveedor(@Path("id") proveedorId: String): Call<List<Producto>>

    @GET("Ciudad")
    fun getCiudades(): Call<List<Ciudad>>

    @GET("Zona")
    fun getZonasPorCiudad(@Path("ciudadId") ciudadId: String): Call<List<Zona>>

    @GET("Cliente")
    fun getClientesPorZona(): Call<List<Cliente>>

    @GET("Pedidos/ConsultarPorCliente/{id}")
    fun getPedidosPorCliente(@Path("id") clienteId: String): Call<List<Pedido>>

}
