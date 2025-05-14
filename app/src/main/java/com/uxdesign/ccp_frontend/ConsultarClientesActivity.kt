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

class ConsultarClientesActivity : AppCompatActivity() {
    private val clientes = mutableListOf<Cliente>()
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_consultar_clientes)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewClientes)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter = ClienteAdapter(clientes)
        recyclerView.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicio-cliente-596275467600.us-central1.run.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        getClientes()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getClientes() {
        val idZona = "11e86372-1b67-4d4b-b234-53f716dab601"
        apiService.getClientesPorZona(idZona).enqueue(object : Callback<RespuestaCliente> {
            override fun onResponse(call: Call<RespuestaCliente>, response: Response<RespuestaCliente>) {
                if (response.isSuccessful) {
                    val clienteList = response.body()?.clientes ?: emptyList()
                    clientes.clear()
                    clientes.addAll(clienteList)
                    (findViewById<RecyclerView>(R.id.recyclerViewClientes).adapter as ClienteAdapter).notifyDataSetChanged()
                } else {
                    Toast.makeText(this@ConsultarClientesActivity, "Error al cargar los clientes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RespuestaCliente>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@ConsultarClientesActivity, "Error de conexión en consultar clientes", Toast.LENGTH_SHORT).show()
            }
        })
    }
}