package com.uxdesign.ccp_frontend

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uxdesign.cpp.R

class GenerarRecomendacionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_generar_recomendacion)

        //val imagen: ImageView = findViewById(R.id.imagenRecomendacion)

        //val bitmap = intent.getParcelableExtra<Bitmap>("imageBitmap")
        //imagen.setImageBitmap(bitmap)

        val minuto = intent.getStringExtra("minuto") ?: "mm"

        val textoCuadro: TextView = findViewById(R.id.textoCuadro)
        val textoConMinuto = "Esta imagen pertenece al minuto $minuto de tu video"
        textoCuadro.text = textoConMinuto


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}