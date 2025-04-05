package com.uxdesign.ccp_frontend

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uxdesign.cpp.R

class FinalizarPedidoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_finalizar_pedido)

        //Adaptabilidad
        val mainLayout: ConstraintLayout = findViewById(R.id.main)
        val titleCliente: TextView = findViewById(R.id.tituloCliente)
        val titleDate: TextView = findViewById(R.id.tituloFechaEntrega)
        val titleHora: TextView = findViewById(R.id.tituloHora)
        val titleCantidad: TextView = findViewById(R.id.tituloNumProductos)
        val titleTotal: TextView = findViewById(R.id.tituloTotal)
        val titleComentarios: TextView = findViewById(R.id.tituloComentarios)
        val buttonRegistrar: Button = findViewById(R.id.buttonRegistrar)
        val imageEye: ImageView = findViewById(R.id.imageOjoN)

        imageEye.visibility = View.VISIBLE

        imageEye.setOnClickListener {


        //val buttonOjo: Button = findViewById(R.id.botonOjo)
        //buttonOjo.setOnClickListener {
        // mainLayout.setBackgroundColor(resources.getColor(R.color.darkgrey, null))
        // titleCliente.setTextColor(resources.getColor(R.color.greytext, null))
        // titleDate.setTextColor(resources.getColor(R.color.greytext, null))
        // titleHora.setTextColor(resources.getColor(R.color.greytext, null))
        // titleCantidad.setTextColor(resources.getColor(R.color.greytext, null))
        // titleTotal.setTextColor(resources.getColor(R.color.greytext, null))
        // titleComentarios.setTextColor(resources.getColor(R.color.greytext, null))
        // buttonRegistrar.setBackgroundColor(resources.getColor(R.color.greytext, null))
            imageEye.setImageResource(R.drawable.blackeye)
            imageEye.visibility = View.GONE

        }

        //User interface
        // mainLayout.setBackgroundColor(resources.getColor(R.color.orange, null))
        // titleCliente.setTextColor(resources.getColor(R.color.pink, null))
        // titleDate.setTextColor(resources.getColor(R.color.pink, null))
        // titleHora.setTextColor(resources.getColor(R.color.pink, null))
        // titleCantidad.setTextColor(resources.getColor(R.color.pink, null))
        // titleTotal.setTextColor(resources.getColor(R.color.pink, null))
        // titleComentarios.setTextColor(resources.getColor(R.color.pink, null))



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}