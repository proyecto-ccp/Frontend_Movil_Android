package com.uxdesign.ccp_frontend

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    //@Multipart
    //@POST("https://servicio-video-596275467600.us-central1.run.app/api/Video/CargarVideo/")
    //fun uploadVideo(
     //   @Part video: MultipartBody.Part,
      //  @Part("data") data: RequestBody
    //): Call<ResponseBody>
    @POST("Video/CargarVideo")  // Reemplaza con la URL de tu microservicio
    fun uploadVideo(@Body videoRequest: VideoRequest): Call<ResponseBody>

}
