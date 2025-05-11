package com.uxdesign.ccp_frontend

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uxdesign.cpp.R

class DetalleVisitaActivity : AppCompatActivity() {
    private lateinit var adapter: VisitaAdapter
    private lateinit var visitas: List<VisitaRequest>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_visita)

        visitas = intent.getSerializableExtra("visitas_filtradas") as List<VisitaRequest>

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewDetalleVisita)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = VisitaAdapter(visitas)
        recyclerView.adapter = adapter

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}