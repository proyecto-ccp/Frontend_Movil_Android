package com.uxdesign.ccp_frontend

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uxdesign.cpp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListaVideosActivity : AppCompatActivity() {
    private val videos = mutableListOf<Video>()
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista_videos)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewVideos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = VideoAdapter(videos)
        recyclerView.adapter = adapter

        val clienteId = intent.getStringExtra("CLIENTE_ID")
        if (clienteId.isNullOrEmpty()) {
            Toast.makeText(this, "ID de cliente no recibido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        getVideos(clienteId)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    private fun getVideos(clienteId: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicio-video-596275467600.us-central1.run.app/api/") // URL base del microservicio
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        apiService = retrofit.create(ApiService::class.java)

        apiService.getVideosPorCliente(clienteId).enqueue(object : Callback<RespuestaVideo> {
            override fun onResponse(call: Call<RespuestaVideo>, response: Response<RespuestaVideo>) {
                if (response.isSuccessful) {
                    val videoList = response.body()?.videos ?: emptyList()
                    if (videoList != null) {
                        videos.clear()
                        videos.addAll(videoList)
                        // Notificar al adaptador que los datos han cambiado
                        (findViewById<RecyclerView>(R.id.recyclerViewVideos).adapter as VideoAdapter).notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this@ListaVideosActivity, "Error al cargar los videos", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RespuestaVideo>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@ListaVideosActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}