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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class RegistrarVisitaActivity : AppCompatActivity() {
    private lateinit var spinnerCliente: Spinner
    private lateinit var editFecha: EditText
    private lateinit var editHora: EditText
    private lateinit var spinnerMotivo: Spinner
    private var listaClientes: List<Cliente> = emptyList()
    private var selectedClienteId: String = ""
    private var selectedMotivo: String = ""
    private lateinit var vendedor: Vendedor
    private val listaMotivos = listOf(
        "Selecciona uno",
        "Presentación de producto",
        "Seguimiento de pedido",
        "Cobranza",
        "Visita de cortesía",
        "Otro"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_visita)

        val idUsuario = intent.getStringExtra("id_usuario")

        spinnerCliente = findViewById(R.id.spinnerCliente)
        editFecha = findViewById(R.id.editFecha)
        editHora = findViewById(R.id.editHora)
        spinnerMotivo = findViewById(R.id.spinnerMotivo)
        val adapterMotivos = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listaMotivos
        )
        adapterMotivos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMotivo.adapter = adapterMotivos


        val buttonRegistrar: Button = findViewById(R.id.buttonRegistrar)

        obtenerZonaVendedor(idUsuario)

        buttonRegistrar.setOnClickListener {
            if (idUsuario.isNullOrEmpty()) {
                Toast.makeText(this, "ID de usuario no disponible", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val posicionCliente = spinnerCliente.selectedItemPosition
            val clienteSeleccionado = listaClientes[posicionCliente - 1]
            val idCliente = clienteSeleccionado.id
            selectedClienteId = idCliente

            val motivoSeleccionado = spinnerMotivo.selectedItem.toString()
            selectedMotivo = motivoSeleccionado

            if (!validarCampos()) {
                return@setOnClickListener
            }

            val fecha = editFecha.text.toString().trim()
            val hora = editHora.text.toString().trim()

            val visita = Visita(
                idCliente = selectedClienteId,
                fecha = fecha,
                hora = hora,
                motivo = motivoSeleccionado,
                estado = "PENDIENTE"
            )

            registrarVisita(visita, idUsuario)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarClientesDesdeApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicio-cliente-596275467600.us-central1.run.app/api/") // Reemplaza por la base real
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val idZona = vendedor.idzona

        apiService.getClientesPorZona(idZona).enqueue(object : Callback<RespuestaCliente> {
            override fun onResponse(call: Call<RespuestaCliente>, response: Response<RespuestaCliente>) {
                if (response.isSuccessful) {
                    val clientes = response.body()?.clientes ?: emptyList()

                    listaClientes = clientes

                    val nombresClientes = clientes.map { "${it.nombre} ${it.apellido}" }
                    val adapter = ArrayAdapter(
                        this@RegistrarVisitaActivity,
                        android.R.layout.simple_spinner_item,
                        listOf("Selecciona uno") + nombresClientes
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerCliente.adapter = adapter
                } else {
                    Toast.makeText(this@RegistrarVisitaActivity, "Error al cargar clientes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RespuestaCliente>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@RegistrarVisitaActivity, "Error de conexión con clientes", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun obtenerZonaVendedor(idUsuario: String?) {
        if (idUsuario.isNullOrEmpty()) {
            Toast.makeText(this, "ID de usuario no disponible", Toast.LENGTH_SHORT).show()
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://vendedor-596275467600.us-central1.run.app/api/") // Reemplaza por la base real
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getVendedor(idUsuario).enqueue(object : Callback<RespuestaVendedor> {
            override fun onResponse(
                call: Call<RespuestaVendedor>,
                response: Response<RespuestaVendedor>
            ) {
                if (response.isSuccessful) {
                    val vendedorS = response.body()?.vendedor

                    if (vendedorS != null) {
                        vendedor = vendedorS
                        cargarClientesDesdeApi()
                    } else {
                        showToast("Datos de vendedor no válidos")
                    }

                } else {
                    Toast.makeText(
                        this@RegistrarVisitaActivity,
                        "Error al cargar clientes",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<RespuestaVendedor>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(
                    this@RegistrarVisitaActivity,
                    "Error de conexión con clientes",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun validarCampos(): Boolean {
        if (spinnerCliente.selectedItem == null || spinnerCliente.selectedItem.toString().isEmpty()) {
            showToast("Seleccione un cliente")
            return false
        }

        if (spinnerMotivo.selectedItem == null || spinnerMotivo.selectedItem.toString().isEmpty()) {
            showToast("Seleccione un motivo de visita")
            return false
        }

        if (editFecha.text.toString().trim().isEmpty()) {
            showToast("Ingrese la fecha de entrega")
            return false
        }

        if (!validarFecha(editFecha.text.toString().trim())) {
            showToast("La fecha de visita debe tener el formato yyyy-MM-dd")
            return false
        }

        if (!validarHora(editHora.text.toString().trim())) {
            showToast("La hora de visista debe tener el formato HH:SS (hora militar)")
            return false
        }

        return true

    }

    fun validarHora(hora: String): Boolean {
        val regex = "^([01]?[0-9]|2[0-3]):([0-5]?[0-9])$".toRegex()
        return hora.matches(regex)
    }

    private fun validarFecha(fecha: String): Boolean {
        val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        formato.isLenient = false

        return try {
            val date = formato.parse(fecha)

            fecha == formato.format(date)
        } catch (e: ParseException) {
            false
        }
    }

    private fun registrarVisita(visita: Visita, idUsuario: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicio-pedidos-596275467600.us-central1.run.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.crearVisita(visita).enqueue(object : Callback<RespuestaRequest> {
            override fun onResponse(call: Call<RespuestaRequest>, response: Response<RespuestaRequest>) {
                if (response.isSuccessful) {
                    val respuesta = response.body()
                    Toast.makeText(this@RegistrarVisitaActivity, "La visita ha sido registrada exitosamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegistrarVisitaActivity, MenuActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {

                        Toast.makeText(this@RegistrarVisitaActivity, "No fue posible crear la visita, intente de nuevo", Toast.LENGTH_SHORT).show()
                    }

            }

            override fun onFailure(call: Call<RespuestaRequest>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@RegistrarVisitaActivity, "Error de conexión con visita", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
