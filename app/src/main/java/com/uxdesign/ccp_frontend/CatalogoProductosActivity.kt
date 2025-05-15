package com.uxdesign.ccp_frontend

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uxdesign.cpp.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CatalogoProductosActivity : AppCompatActivity() {

    private val productos = mutableListOf<Producto>()
    private lateinit var catalogoManager: CatalogoManager

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

        val apiService = retrofit.create(ApiService::class.java)

        catalogoManager = CatalogoManager(apiService)

        catalogoManager.obtenerCatalogo(object : CatalogoManager.CatalogoCallback {
            override fun onExito(productos: List<Producto>) {
                this@CatalogoProductosActivity.productos.clear()
                this@CatalogoProductosActivity.productos.addAll(productos)
                adapter.notifyDataSetChanged()
            }

            override fun onError(mensaje: String) {
                Toast.makeText(this@CatalogoProductosActivity, mensaje, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
