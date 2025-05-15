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
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConsultarClientesActivity : AppCompatActivity() {
    private val clientes = mutableListOf<Cliente>()
    private lateinit var consultasClientesManager: ConsultasClientesManager

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

        val apiService = retrofit.create(ApiService::class.java)
        consultasClientesManager = ConsultasClientesManager(apiService)

        val idZona = "11e86372-1b67-4d4b-b234-53f716dab601"
        consultasClientesManager.obtenerClientesPorZona(idZona, object : ConsultasClientesManager.ClientesCallback {
            override fun onExito(clientes: List<Cliente>) {
                this@ConsultarClientesActivity.clientes.clear()
                this@ConsultarClientesActivity.clientes.addAll(clientes)
                adapter.notifyDataSetChanged()
            }

            override fun onError(mensaje: String) {
                Toast.makeText(this@ConsultarClientesActivity, mensaje, Toast.LENGTH_SHORT).show()
            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
