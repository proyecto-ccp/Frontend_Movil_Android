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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListaVideosActivity : AppCompatActivity() {
    private val videos = mutableListOf<Video>()
    private lateinit var videoDataManager: VideoDataManager

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

        // Inicializamos Retrofit y el manager
        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicio-video-596275467600.us-central1.run.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        videoDataManager = VideoDataManager(apiService)

        // Usamos la clase separada
        videoDataManager.getVideosPorCliente(
            clienteId,
            onSuccess = { videoList ->
                videos.clear()
                videos.addAll(videoList)
                adapter.notifyDataSetChanged()
            },
            onEmpty = {
                Toast.makeText(
                    this,
                    "No hay videos cargados para el cliente seleccionado",
                    Toast.LENGTH_SHORT
                ).show()
            },
            onError = {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        )

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
