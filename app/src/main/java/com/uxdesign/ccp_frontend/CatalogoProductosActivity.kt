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
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CatalogoProductosActivity : AppCompatActivity() {
    private lateinit var idUsuario: String
    private val productos = mutableListOf<Producto>()
    private lateinit var catalogoManager: CatalogoManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_catalogo_productos)

        val buttonPedido: Button = findViewById(R.id.botonPedido)
        val buttonMenu: Button = findViewById(R.id.botonVolverMenu)

        idUsuario = intent.getStringExtra("id_usuario") ?: ""

        buttonMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("id_usuario", idUsuario)
            startActivity(intent)
        }

        buttonPedido.setOnClickListener {
            val intent = Intent(this, VerPedidoActivity::class.java)
            intent.putExtra("id_usuario", idUsuario)
            startActivity(intent)
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = ProductoAdapter(productos, idUsuario)
        recyclerView.adapter = adapter

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(this))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://productos-596275467600.us-central1.run.app/api/")
            .client(client)
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
