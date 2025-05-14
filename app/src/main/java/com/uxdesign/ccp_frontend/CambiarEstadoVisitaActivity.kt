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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uxdesign.cpp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CambiarEstadoVisitaActivity : AppCompatActivity() {
    private lateinit var spinnerEstado: Spinner
    private var selectedClienteId: String = ""
    private lateinit var editDescripcion: EditText
    private val listaEstados = listOf(
        "Selecciona uno",
        "REALIZADA",
        "PENDIENTE",
        "CANCELADA"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cambiar_estado_visita)

        spinnerEstado = findViewById(R.id.spinnerEstados)
        editDescripcion = findViewById(R.id.editDescripcion)

        spinnerEstado = findViewById(R.id.spinnerEstados)
        val adapterEstados = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listaEstados
        )
        adapterEstados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEstado.adapter = adapterEstados

        val visita = intent.getSerializableExtra("visita") as VisitaRequest

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

            actualizarEstadoVisita(actualizacion)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun actualizarEstadoVisita(visita: VisitaRequest) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicio-visitas-596275467600.us-central1.run.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.modificarEstadoVisita(visita.id, visita).enqueue(object : Callback<RespuestaRequest> {
            override fun onResponse(call: Call<RespuestaRequest>, response: Response<RespuestaRequest>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CambiarEstadoVisitaActivity, "La visita ha sido actualizada exitosamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@CambiarEstadoVisitaActivity, MenuActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {

                    Toast.makeText(this@CambiarEstadoVisitaActivity, "No fue posible actualizar la visita, intente de nuevo", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RespuestaRequest>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@CambiarEstadoVisitaActivity, "Error de conexión con visitas", Toast.LENGTH_SHORT).show()
            }
        })
    }
}