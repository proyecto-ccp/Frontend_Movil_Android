package com.uxdesign.ccp_frontend

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

        val buttonPedido: Button = findViewById(R.id.botonPedido)

        val idUsuario = "b07e8ab8-b787-4f6d-8a85-6c506a3616f5"

        buttonPedido.setOnClickListener {
            val intent = Intent(this, VerPedidoActivity::class.java)
            intent.putExtra("id_usuario", idUsuario)
            startActivity(intent)
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = ProductoAdapter(productos)
        recyclerView.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://productos-596275467600.us-central1.run.app/api/")
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
        apiService.getProductos().enqueue(object : Callback<RespuestaProducto> {
            override fun onResponse(call: Call<RespuestaProducto>, response: Response<RespuestaProducto>) {
                if (response.isSuccessful) {
                    val productoList = response.body()?.productos ?: emptyList()
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

            override fun onFailure(call: Call<RespuestaProducto>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@CatalogoProductosActivity, "Error de conexión en catálogo", Toast.LENGTH_SHORT).show()
            }
        })
    }
}