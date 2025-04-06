package com.uxdesign.ccp_frontend

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uxdesign.cpp.R

class ListaVideosActivity : AppCompatActivity() {
    private val videos = mutableListOf<Video>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista_videos)

        val idProducto = intent.getIntExtra("idProducto" , -1)
        val idCliente = intent.getStringExtra("idCliente") ?: null
        val videoName = intent.getStringExtra("videoName") ?: null

        if (videoName != null && idProducto != -1 && idCliente != null) {
            val video = Video(videoName, idCliente, idProducto.toString(), "En Proceso")
            videos.add(video)
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewVideos)
        recyclerView.layoutManager = LinearLayoutManager(this)


        val adapter = VideoAdapter(videos)
        recyclerView.adapter = adapter


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}