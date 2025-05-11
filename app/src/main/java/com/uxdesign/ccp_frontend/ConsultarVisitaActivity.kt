package com.uxdesign.ccp_frontend

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uxdesign.cpp.R

class ConsultarVisitaActivity : AppCompatActivity() {
    private lateinit var editTextFecha: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_consultar_visita)

        val idUsuario = intent.getStringExtra("id_usuario")
        editTextFecha = findViewById(R.id.editFecha)
        val buttonConsultar: Button = findViewById(R.id.buttonConsultar)

        buttonConsultar.setOnClickListener {
            val fecha = editTextFecha.text.toString()
            if (idUsuario.isNullOrEmpty()) {
                Toast.makeText(this, "ID de usuario no disponible", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else if (fecha.isEmpty()) {
                Toast.makeText(this, "Debes ingresar una fecha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else{
                val intent = Intent(this, ListarVisitasActivity::class.java)
                intent.putExtra("fecha", fecha)
                intent.putExtra("idUsuario", idUsuario)
                startActivity(intent)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}