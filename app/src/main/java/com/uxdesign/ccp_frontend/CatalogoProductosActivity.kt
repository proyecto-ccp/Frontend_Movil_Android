package com.uxdesign.ccp_frontend

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uxdesign.cpp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CatalogoProductosActivity : AppCompatActivity() {
    private val productos = mutableListOf<Producto>()
    private lateinit var apiService: ApiService

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

        val adapter = ProductoAdapter(productos)
        recyclerView.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://catalogo")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        getCatalogo()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getCatalogo() {
        apiService.getProductos().enqueue(object : Callback<List<Producto>> {
            override fun onResponse(call: Call<List<Producto>>, response: Response<List<Producto>>) {
                if (response.isSuccessful) {
                    val productoList = response.body()
                    if (productoList != null) {
                        productos.clear()
                        productos.addAll(productoList)
                        // Notificar al adaptador que los datos han cambiado
                        (findViewById<RecyclerView>(R.id.recyclerViewProductos).adapter as ProductoAdapter).notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this@CatalogoProductosActivity, "Error al cargar el catálogo", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Producto>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@CatalogoProductosActivity, "Error de conexión en catálogo", Toast.LENGTH_SHORT).show()
            }
        })
    }
}