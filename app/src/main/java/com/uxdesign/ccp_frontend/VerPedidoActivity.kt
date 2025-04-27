package com.uxdesign.ccp_frontend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
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

class VerPedidoActivity : AppCompatActivity() {
    private val detallePedido = mutableListOf<ProductoCarrito>()
    private lateinit var apiService: ApiService
    private var totalProductos: Int = 0
    private var valorTotal: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ver_pedido)
        val idUsuario = intent.getStringExtra("id_usuario")

        //Adaptabilidad
        val mainLayout: ConstraintLayout = findViewById(R.id.main)
        val titleTotal: TextView = findViewById(R.id.tituloTotal)
        val titleCantidad: TextView = findViewById(R.id.tituloNumProductos)
        val buttonFin: Button = findViewById(R.id.buttonFin)
        val imageEye: ImageView = findViewById(R.id.imageOjoN)

        imageEye.visibility = View.GONE

        imageEye.setOnClickListener {


        //val buttonOjo: Button = findViewById(R.id.botonOjo)
        //buttonOjo.setOnClickListener {
        // mainLayout.setBackgroundColor(resources.getColor(R.color.darkgrey, null))
        // titleTotal.setTextColor(resources.getColor(R.color.greytext, null))
        // titleCantidad.setTextColor(resources.getColor(R.color.greytext, null))
        // buttonFin.setBackgroundColor(resources.getColor(R.color.greytext, null))
        imageEye.setImageResource(R.drawable.blackeye)
        imageEye.visibility = View.GONE
        }

        //User interface
        // mainLayout.setBackgroundColor(resources.getColor(R.color.orange, null))
        // titleTotal.setTextColor(resources.getColor(R.color.pink, null))
        // titleCantidad.setTextColor(resources.getColor(R.color.pink, null))


        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewProductosPedido)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = ProductoPedidoAdapter(detallePedido)
        recyclerView.adapter = adapter

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

        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicio-pedidos-596275467600.us-central1.run.app/api/") // URL base del microservicio
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
        apiService.getDetallePedidoUsuario(idUsuario).enqueue(object : Callback<RespuestaDetalleCarrito> {
            override fun onResponse(call: Call<RespuestaDetalleCarrito>, response: Response<RespuestaDetalleCarrito>) {
                if (response.isSuccessful) {
                    val detallePedidoList = response.body()?.detallePedidos ?: emptyList()
                    if (detallePedidoList != null) {
                        detallePedido.clear()
                        detallePedido.addAll(detallePedidoList)

                        totalProductos = detallePedidoList.size
                        valorTotal = detallePedidoList.sumOf { it.precioUnitario * it.cantidad }
                        val valor = "$${String.format("%.2f", valorTotal)}"

                        val editCantidad: EditText = findViewById(R.id.editNumProductos)
                        val editValor: EditText = findViewById(R.id.editTotal)
                        editCantidad.setText(totalProductos.toString())
                        editValor.setText(valor)

                        (findViewById<RecyclerView>(R.id.recyclerViewProductosPedido).adapter as ProductoPedidoAdapter).notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this@VerPedidoActivity, "Error al cargar detalle de pedido", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RespuestaDetalleCarrito>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@VerPedidoActivity, "Error de conexión en detalle de pedido", Toast.LENGTH_SHORT).show()
            }
        })
    }
}