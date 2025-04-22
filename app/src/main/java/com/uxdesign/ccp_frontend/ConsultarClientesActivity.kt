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
            .baseUrl("https://clientes")
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
        apiService.getClientesPorZona().enqueue(object : Callback<List<Cliente>> {
            override fun onResponse(call: Call<List<Cliente>>, response: Response<List<Cliente>>) {
                if (response.isSuccessful) {
                    val clienteList = response.body()
                    if (clienteList != null) {
                        clientes.clear()
                        clientes.addAll(clienteList)
                        // Notificar al adaptador que los datos han cambiado
                        (findViewById<RecyclerView>(R.id.recyclerViewClientes).adapter as ClienteAdapter).notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this@ConsultarClientesActivity, "Error al cargar los clientes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Cliente>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@ConsultarClientesActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })

    }
}