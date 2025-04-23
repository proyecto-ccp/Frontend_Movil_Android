package com.uxdesign.ccp_frontend

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.uxdesign.cpp.R
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Callback
import retrofit2.Response


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

        Glide.with(this)
            .load(productoImagen) // URL de la imagen
            .placeholder(R.drawable.logoccppeque) // opcional
            .error(R.drawable.logoccppeque) // opcional
            .into(imageProducto)

        val editCantidad: TextView = findViewById(R.id.editCantidad)

        agregarButton.setOnClickListener {
            val cantidadText = editCantidad.text.toString().trim()

            if (cantidadText.isEmpty() || cantidadText == "0") {
                Toast.makeText(this, "Por favor ingrese una cantidad válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cantidad = cantidadText.toInt()
            val idUsuario = "12345"

            val productoRequest = ProductoCarrito(
                idProducto = productoId ?: "",
                cantidad = cantidad,
                idUsuario = idUsuario
            )
            val retrofit = Retrofit.Builder()
                .baseUrl("https://tuservidor.com/api/") // Cambia a tu URL real
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val api = retrofit.create(ApiService::class.java)

            api.agregarProducto(productoRequest).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@DetalleProductoActivity, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@DetalleProductoActivity, VerPedidoActivity::class.java))
                    } else {
                        Toast.makeText(this@DetalleProductoActivity, "Error al agregar", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@DetalleProductoActivity, "Fallo la conexión del carrito", Toast.LENGTH_SHORT).show()
                }
            })

            }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}