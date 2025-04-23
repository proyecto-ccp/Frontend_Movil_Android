package com.uxdesign.ccp_frontend

import android.os.Bundle
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

class ConsultarEstadoPedidosActivity : AppCompatActivity() {
    private val pedidos = mutableListOf<Pedido>()
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_consultar_estado_pedidos)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewEstadoPedidos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = PedidoAdapter(pedidos)
        recyclerView.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://pedidos")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        getPedidos()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }


    private fun getPedidos() {
          apiService.getPedidosPorCliente("").enqueue(object : Callback<List<Pedido>> {
               override fun onResponse(call: Call<List<Pedido>>, response: Response<List<Pedido>>) {
                    if (response.isSuccessful) {
                        val pedidoList = response.body()
                        if (pedidoList != null) {
                            pedidos.clear()
                            pedidos.addAll(pedidoList)
                            // Notificar al adaptador que los datos han cambiado
                            (findViewById<RecyclerView>(R.id.recyclerViewEstadoPedidos).adapter as PedidoAdapter).notifyDataSetChanged()
                        }
                    } else {
                        Toast.makeText(this@ConsultarEstadoPedidosActivity, "Error al cargar los estados de pedidos", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Pedido>>, t: Throwable) {
                    t.printStackTrace()
                    Toast.makeText(this@ConsultarEstadoPedidosActivity, "Error de conexión de pedidos", Toast.LENGTH_SHORT).show()
                }
          })

    }

}