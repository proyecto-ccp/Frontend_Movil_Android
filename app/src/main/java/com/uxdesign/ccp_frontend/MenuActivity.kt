package com.uxdesign.ccp_frontend

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uxdesign.cpp.R

class MenuActivity : AppCompatActivity() {
    private lateinit var idUsuario: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        idUsuario = intent.getStringExtra("id_usuario") ?: "desconocido"

        val buttonRecomendar: Button = findViewById(R.id.buttonRecomendar)
        buttonRecomendar.setOnClickListener {
            val intent = Intent(this, CargarVideoActivity::class.java)
            intent.putExtra("id_usuario", idUsuario)
            startActivity(intent)
        }

        val buttonCatalogo: Button = findViewById(R.id.buttonPedido)
        buttonCatalogo.setOnClickListener {
            val intent = Intent(this, CatalogoProductosActivity::class.java)
            intent.putExtra("id_usuario", idUsuario)
            startActivity(intent)
        }

        val buttonConsultarCliente: Button = findViewById(R.id.buttonConsultarCliente)
        buttonConsultarCliente.setOnClickListener {
            val intent = Intent(this, ConsultarClientesActivity::class.java)
            intent.putExtra("id_usuario", idUsuario)
            startActivity(intent)
        }

        val buttonCerrarSesion: Button = findViewById(R.id.buttonCerrarSesion)
        buttonCerrarSesion.setOnClickListener {
            TokenManager.clearToken(this)
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        val buttonRegistrarVisita: Button = findViewById(R.id.buttonRegistrarVisita)
        buttonRegistrarVisita.setOnClickListener {
            val intent = Intent(this, RegistrarVisitaActivity::class.java)
            intent.putExtra("id_usuario", idUsuario)
            startActivity(intent)
        }

        val buttonRutaVisita: Button = findViewById(R.id.buttonRutaVisita)
        buttonRutaVisita.setOnClickListener {
            val intent = Intent(this, ConsultarVisitaActivity::class.java)
            intent.putExtra("id_usuario", idUsuario)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}