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

    @Multipart
    @POST("tu/endpoint/aqui")
    fun uploadVideo(
        @Part videoPart: MultipartBody.Part,          // El video (archivo)
        @Part("producto") productoBody: RequestBody   // El nombre del producto (texto)
    ): Call<ResponseBody>
}
