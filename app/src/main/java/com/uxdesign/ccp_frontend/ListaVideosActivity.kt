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

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.tuservicio.com/") // URL base del microservicio
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        getVideos()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    private fun getVideos() {
        apiService.getRecomendacion().enqueue(object : Callback<List<Video>> {
            override fun onResponse(call: Call<List<Video>>, response: Response<List<Video>>) {
                if (response.isSuccessful) {
                    val videoList = response.body()
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

            override fun onFailure(call: Call<List<Video>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@ListaVideosActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }
}