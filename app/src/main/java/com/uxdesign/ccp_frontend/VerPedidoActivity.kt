package com.uxdesign.ccp_frontend

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uxdesign.cpp.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VerPedidoActivity : AppCompatActivity() {
    private val detallePedido = mutableListOf<ProductoCarrito>()
    private lateinit var pedidoRepository: PedidoRepository
    private var totalProductos: Int = 0
    private var valorTotal: Double = 0.0
    private lateinit var adapter: ProductoPedidoAdapter
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_pedido)

        idUsuario = intent.getStringExtra("id_usuario")

        val buttonFin: Button = findViewById(R.id.buttonFin)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewProductosPedido)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProductoPedidoAdapter(detallePedido)
        recyclerView.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicio-pedidos-596275467600.us-central1.run.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        pedidoRepository = PedidoRepository(apiService)

        cargarDetallesDesdeApi(idUsuario)

        buttonFin.setOnClickListener {
            val intent = Intent(this, FinalizarPedidoActivity::class.java)
            intent.putExtra("id_usuario", idUsuario)
            intent.putExtra("cantidad_productos", totalProductos)
            intent.putExtra("valor_total", valorTotal)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarDetallesDesdeApi(idUsuario: String?) {
        if (idUsuario.isNullOrEmpty()) {
            Toast.makeText(this, "ID de usuario no disponible", Toast.LENGTH_SHORT).show()
            return
        }

        pedidoRepository.obtenerDetallePedido(
            idUsuario,
            onSuccess = { detalle ->
                detallePedido.clear()
                detallePedido.addAll(detalle)

                totalProductos = detalle.size
                valorTotal = detalle.sumOf { it.precioUnitario * it.cantidad }

                val editCantidad: EditText = findViewById(R.id.editNumProductos)
                val editValor: EditText = findViewById(R.id.editTotal)
                editCantidad.setText(totalProductos.toString())
                editValor.setText("$${String.format("%.2f", valorTotal)}")

                adapter.notifyDataSetChanged()
            },
            onError = { mensaje ->
                Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                val buttonFin: Button = findViewById(R.id.buttonFin)
                buttonFin.isEnabled = false
            }
        )
    }
}
