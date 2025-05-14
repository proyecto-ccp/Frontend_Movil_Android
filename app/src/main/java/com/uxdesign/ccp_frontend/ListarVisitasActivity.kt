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

class ListarVisitasActivity : AppCompatActivity() {
    private val visitas = mutableListOf<VisitaRequest>()
    private var ciudadVisitas = mutableListOf<CiudadVisita>()
    private lateinit var apiService: ApiService
    private lateinit var adapter: CiudadVisitaAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listar_visitas)

        val fecha = intent.getStringExtra("fecha")
        val idUsuario = intent.getStringExtra("idUsuario")

        if (idUsuario.isNullOrEmpty() || fecha.isNullOrEmpty()) {
            Toast.makeText(this, "ID de vendedor o fecha no recibida", Toast.LENGTH_SHORT).show()
            finish()
            return        }

        cargarCiudadVisitas(fecha, idUsuario)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewCiudadVisitas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CiudadVisitaAdapter(ciudadVisitas, visitas)
        recyclerView.adapter = adapter


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarCiudadVisitas(fecha: String, idUsuario: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicio-visitas-596275467600.us-central1.run.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        apiService = retrofit.create(ApiService::class.java)
        val fechaFormateada = "${fecha}T00:00:00.420Z"
        apiService.getVisitasPorFecha(fechaFormateada, idUsuario).enqueue(object : Callback<RespuestaVisita> {
            override fun onResponse(call: Call<RespuestaVisita>, response: Response<RespuestaVisita>) {
                if (response.isSuccessful) {
                    val visitaList = response.body()?.visitas ?: emptyList()
                    if (visitaList.isEmpty()) {
                        Toast.makeText(
                            this@ListarVisitasActivity,
                            "No hay visitas registradas para la fecha seleccionada",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        visitas.clear()
                        visitas.addAll(visitaList)
                        extraerCiudades()
                    }
                } else {
                    Toast.makeText(this@ListarVisitasActivity, "No tiene visitas registradas para la fecha seleccionada", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RespuestaVisita>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@ListarVisitasActivity, "Error de conexión con visitas", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun extraerCiudades() {
        val visitasPorCiudad = mutableMapOf<Pair<String, String>, Int>()
        for (visita in visitas) {
            val ciudad = visita.cliente?.ciudad ?: "Sin ciudad"
            val fecha = visita.fechaVisita?.substring(0, 10) ?: "Sin fecha"
            val key = ciudad to fecha
            visitasPorCiudad[key] = visitasPorCiudad.getOrDefault(key, 0) + 1
        }

        ciudadVisitas.clear()
        ciudadVisitas.addAll(
            visitasPorCiudad.map { (key, cantidad) ->
                CiudadVisita(key.first, cantidad, key.second)
            }
        )
        adapter.notifyDataSetChanged()
    }
}