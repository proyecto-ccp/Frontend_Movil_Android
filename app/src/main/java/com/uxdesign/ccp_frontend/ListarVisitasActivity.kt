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

class ListarVisitasActivity : AppCompatActivity() {
    private val visitas = mutableListOf<VisitaRequest>()
    private var ciudadVisitas = mutableListOf<CiudadVisita>()
    private lateinit var adapter: CiudadVisitaAdapter


    // Inyectamos el ApiService y el VisitaDataManager
    private val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://servicio-visitas-596275467600.us-central1.run.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    private val visitaDataManager by lazy {
        VisitaDataManager(apiService)
    }


    private lateinit var idUsuario: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_listar_visitas)

        val fecha = intent.getStringExtra("fecha")
        idUsuario = intent.getStringExtra("id_usuario") ?: ""

        if (idUsuario.isNullOrEmpty() || fecha.isNullOrEmpty()) {
           Toast.makeText(this, "ID de vendedor o fecha no recibida", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewCiudadVisitas)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CiudadVisitaAdapter(ciudadVisitas, visitas)
        recyclerView.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        visitaDataManager.cargarCiudadVisitas(

            fecha,
            idUsuario,
            onSuccess = { listaVisitas ->
                visitas.clear()
                visitas.addAll(listaVisitas)
                ciudadVisitas.clear()
                ciudadVisitas.addAll(visitaDataManager.extraerCiudades(listaVisitas))
                adapter.notifyDataSetChanged()
            },
            onEmpty = {
                Toast.makeText(this, "No hay visitas registradas para la fecha seleccionada", Toast.LENGTH_SHORT).show()
            },
            onError = {
                Toast.makeText(this, "Error de conexión con visitas", Toast.LENGTH_SHORT).show()
            }
        )
    }
}
