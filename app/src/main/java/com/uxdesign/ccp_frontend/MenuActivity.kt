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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        val buttonRecomendar: Button = findViewById(R.id.buttonRecomendar)
        buttonRecomendar.setOnClickListener {
            val intent = Intent(this, CargarVideoActivity::class.java)
            startActivity(intent)
        }

        val buttonCatalogo: Button = findViewById(R.id.buttonPedido)
        buttonCatalogo.setOnClickListener {
            val intent = Intent(this, CatalogoProductosActivity::class.java)
            startActivity(intent)
        }

        val buttonConsultarCliente: Button = findViewById(R.id.buttonConsultarCliente)
        buttonConsultarCliente.setOnClickListener {
            val intent = Intent(this, ConsultarClientesActivity::class.java)
            startActivity(intent)
        }

        val buttonEstadoPedido: Button = findViewById(R.id.buttonConsultarEstadoPedido)
        buttonEstadoPedido.setOnClickListener {
            val intent = Intent(this, ConsultarClientesActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}