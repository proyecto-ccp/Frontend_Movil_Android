package com.uxdesign.ccp_frontend

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uxdesign.cpp.R

class CatalogoProductosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_catalogo_productos)

        //Adaptabilidad

        val mainLayout: ConstraintLayout = findViewById(R.id.main)
        val buttonFinalizar: Button = findViewById(R.id.botonFinalizar)
        val buttonPedido: Button = findViewById(R.id.botonPedido)
        val imageEye: ImageView = findViewById(R.id.imageOjoN)

        imageEye.visibility = View.VISIBLE

        imageEye.setOnClickListener {

            // mainLayout.setBackgroundColor(resources.getColor(R.color.darkgrey, null))
           // buttonFinalizar.setBackgroundColor(resources.getColor(R.color.greytext, null))
           // buttonPedido.setBackgroundColor(resources.getColor(R.color.greytext, null))
            imageEye.setImageResource(R.drawable.blackeye)
            imageEye.visibility = View.GONE
        }

        //User Interface Compras
        // mainLayout.setBackgroundColor(resources.getColor(R.color.orange, null))
        //imageEye.visibility = View.VISIBLE

         buttonFinalizar.setOnClickListener {
            val intent = Intent(this, FinalizarPedidoActivity::class.java)
            startActivity(intent)
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val productos = listOf(
            Producto(1, "Producto 1", "producto", 10, "imagen1"),
            Producto(2, "Producto 2", "producto", 20, "imagen2"),
            Producto(3, "Producto 3", "producto",15, "imagen3"),
            Producto(4, "Producto 4", "producto",30, "imagen4")
        )

        val adapter = ProductoAdapter(productos)
        recyclerView.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}