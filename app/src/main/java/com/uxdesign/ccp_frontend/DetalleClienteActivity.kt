package com.uxdesign.ccp_frontend

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uxdesign.cpp.R

class DetalleClienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_cliente)

        val nombre = intent.getStringExtra("cliente_nombre")
        val apellido = intent.getStringExtra("cliente_apellido")
        val documento = intent.getStringExtra("cliente_documento")
        val direccion = intent.getStringExtra("cliente_direccion")
        val telefono = intent.getStringExtra("cliente_telefono")
        val tipoDocumentoAbreviado = intent.getStringExtra("cliente_tipodoc")
        val ciudad = intent.getStringExtra("cliente_ciudad")
        val zona = intent.getStringExtra("cliente_ciudad")
        val correo = intent.getStringExtra("cliente_correo")

        val tipoDocumento = when (tipoDocumentoAbreviado) {
            "CC" -> "Cédula de Ciudadanía"
            "PS" -> "Pasaporte"
            "NI" -> "Número de Identificación Tributario"
            "CE" -> "Cédula de Extranjería"
            else -> "Tipo desconocido"
        }

        findViewById<TextView>(R.id.textoNombre).text = "$nombre $apellido"
        findViewById<TextView>(R.id.textoTipoDoc).text = "$tipoDocumento"
        findViewById<TextView>(R.id.textoNumDoc).text = "$documento"
        findViewById<TextView>(R.id.textoDireccion).text = "$direccion"
        findViewById<TextView>(R.id.textoTelefono).text = "$telefono"
        findViewById<TextView>(R.id.textoCiudad).text = "$ciudad"
        findViewById<TextView>(R.id.textoZona).text = "$zona"
        findViewById<TextView>(R.id.textoCorreo).text = "$correo"

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}