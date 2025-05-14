package com.uxdesign.ccp_frontend

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uxdesign.cpp.R
import com.bumptech.glide.Glide

class GenerarRecomendacionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_generar_recomendacion)

        val imagen = intent.getStringExtra("video_imagen")

        val imageView: ImageView = findViewById<ImageView>(R.id.imagenRecomendacion)

        if (!imagen.isNullOrEmpty()) {
            Glide.with(this)
                .load(imagen)
                .placeholder(R.drawable.cargando)
                .error(R.drawable.cargando)
                .into(imageView)
        } else {
            Glide.with(this)
                .load(R.drawable.cargando)
                .into(imageView)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}