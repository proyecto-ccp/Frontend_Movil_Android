package com.uxdesign.ccp_frontend

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.uxdesign.cpp.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CambiarEstadoVisitaActivity : AppCompatActivity() {
    private lateinit var spinnerEstado: Spinner
    private lateinit var editDescripcion: EditText
    private lateinit var visitaUpdater: VisitaUpdater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cambiar_estado_visita)

        spinnerEstado = findViewById(R.id.spinnerEstados)
        editDescripcion = findViewById(R.id.editDescripcion)

        val listaEstados = listOf("Selecciona uno", "REALIZADA", "PENDIENTE", "CANCELADA")
        val adapterEstados = ArrayAdapter(this, android.R.layout.simple_spinner_item, listaEstados)
        adapterEstados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEstado.adapter = adapterEstados

        val visita = intent.getSerializableExtra("visita") as VisitaRequest

        visitaUpdater = VisitaUpdater(Retrofit.Builder()
            .baseUrl("https://servicio-visitas-596275467600.us-central1.run.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
        )

        val buttonActualizar: Button = findViewById(R.id.buttonActualizar)
        buttonActualizar.setOnClickListener {
            val nuevoEstado = spinnerEstado.selectedItem.toString()
            val nuevaDescripcion = editDescripcion.text.toString()

            val actualizacion = VisitaRequest(
                id = visita.id,
                idCliente = visita.idCliente,
                idVendedor = visita.idVendedor,
                cliente = visita.cliente,
                fechaVisita = visita.fechaVisita,
                motivo = visita.motivo,
                resultado = nuevaDescripcion,
                estado = nuevoEstado
            )

            visitaUpdater.actualizarVisita(actualizacion,
                onSuccess = {
                    Toast.makeText(this, "La visita ha sido actualizada exitosamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MenuActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                },
                onError = { mensaje ->
                    Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
