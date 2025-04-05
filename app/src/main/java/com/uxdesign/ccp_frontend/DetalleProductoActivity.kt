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
import org.w3c.dom.Text

class DetalleProductoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_producto)

        //Adaptabilidad
        val mainLayout: ConstraintLayout = findViewById(R.id.main)
        val titleCanti: TextView = findViewById(R.id.tituloCantidad)
        val titleValue: TextView = findViewById(R.id.tituloValor)
        val agregarButton: Button = findViewById(R.id.buttonAgregar)
        val imageEye: ImageView = findViewById(R.id.imageOjoN)

        imageEye.visibility = View.VISIBLE

        imageEye.setOnClickListener {


        //val buttonOjo: Button = findViewById(R.id.botonOjo)
        //buttonOjo.setOnClickListener {
        // mainLayout.setBackgroundColor(resources.getColor(R.color.darkgrey, null))
        // titleCanti.setTextColor(resources.getColor(R.color.greytext, null))
        // titleValue.setTextColor(resources.getColor(R.color.greytext, null))
        // agregarButton.setBackgroundColor(resources.getColor(R.color.buttonAgregar, null))
            imageEye.setImageResource(R.drawable.blackeye)
            imageEye.visibility = View.GONE
        }

        //User interface
        // mainLayout.setBackgroundColor(resources.getColor(R.color.orange, null))
        // titleCanti.setTextColor(resources.getColor(R.color.pink, null))
        // titleValue.setTextColor(resources.getColor(R.color.pink, null))

        val productoId = intent.getStringExtra("producto_id")
        val productoNombre = intent.getStringExtra("producto_nombre")
        val productoPrecio = intent.getStringExtra("producto_precio")
        val productoDescripcion = intent.getStringExtra("producto_descripcion")
        val productoImagen = intent.getStringExtra("producto_imagen")

        val imageProducto: ImageView = findViewById(R.id.imageProducto)
        val nombreProducto: TextView = findViewById(R.id.textNombreProducto)
        val precioProducto: TextView = findViewById(R.id.textPrecioProducto)
        val descripcionProducto: TextView = findViewById(R.id.textDescripcionProducto)

        // Establecer los datos recibidos
        nombreProducto.text = productoNombre
        precioProducto.text = "$${productoPrecio}"
        descripcionProducto.text = productoDescripcion
        //imageProducto.setImageResource(productoImagen)
       // Glide.with(this)
         //   .load(productoImagen)
           // .into(imageProducto)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}