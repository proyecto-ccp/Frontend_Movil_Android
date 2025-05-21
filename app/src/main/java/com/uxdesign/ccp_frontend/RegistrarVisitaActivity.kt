package com.uxdesign.ccp_frontend

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.uxdesign.cpp.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class RegistrarVisitaActivity : AppCompatActivity() {
    private lateinit var spinnerCliente: Spinner
    private lateinit var editFecha: EditText
    private lateinit var spinnerMotivo: Spinner
    private var listaClientes: List<Cliente> = emptyList()
    private var selectedClienteId: String = ""
    private var selectedMotivo: String = ""
    private lateinit var vendedor: Vendedor
    private lateinit var listaMotivos: List<String>
    private lateinit var idUsuario: String

    override fun attachBaseContext(newBase: Context) {
        val idioma = obtenerIdiomaGuardado(newBase)
        val context = cambiarIdioma(newBase, idioma)
        super.attachBaseContext(context)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_visita)

        listaMotivos = listOf(
            getString(R.string.selecciona_uno),
            getString(R.string.presentaci_n_de_producto),
            getString(R.string.seguimiento_de_pedido),
            getString(R.string.cobranza),
            getString(R.string.visita_de_cortes_a),
            getString(R.string.otro)
        )

        val ojoIng = findViewById<ImageView>(R.id.imageOjoIng)
        val ojoPor = findViewById<ImageView>(R.id.imageOjoPor)
        val ojoEsp = findViewById<ImageView>(R.id.imageOjoEsp)

        ojoIng.setOnClickListener {
            val idiomaActual = obtenerIdiomaGuardado(this)
            if (idiomaActual != "en") {
                guardarIdioma(this, "en")
                recrearConNuevoIdioma("en")
            }
        }

        ojoPor.setOnClickListener {
            val idiomaActual = obtenerIdiomaGuardado(this)
            if (idiomaActual != "pt") {
                guardarIdioma(this, "pt")
                recrearConNuevoIdioma("pt")
            }
        }

        ojoEsp.setOnClickListener {
            val idiomaActual = obtenerIdiomaGuardado(this)
            if (idiomaActual != "es") {
                guardarIdioma(this, "es")
                recrearConNuevoIdioma("es")
            }
        }

        idUsuario = intent.getStringExtra("id_usuario") ?: ""

        spinnerCliente = findViewById(R.id.spinnerCliente)
        editFecha = findViewById(R.id.editFecha)
        editFecha.setOnClickListener {
            val calendario = Calendar.getInstance()
            val year = calendario[Calendar.YEAR]
            val month = calendario[Calendar.MONTH]
            val day = calendario[Calendar.DAY_OF_MONTH]

            val datePicker = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val fechaSeleccionada = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                    editFecha.setText(fechaSeleccionada)
                },
                year, month, day
            )

            datePicker.show()
        }


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
                Toast.makeText(this,
                    getString(R.string.id_de_usuario_no_disponible), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val motivoSeleccionado = spinnerMotivo.selectedItem.toString()
            selectedMotivo = motivoSeleccionado

            if (!validarCampos()) {
                return@setOnClickListener
            }

            val posicionCliente = spinnerCliente.selectedItemPosition
            val clienteSeleccionado = listaClientes[posicionCliente - 1]
            val idCliente = clienteSeleccionado.id
            selectedClienteId = idCliente

            val fecha = editFecha.text.toString().trim()
            val fechaISO = convertirFechaAISO8601(fecha)
            val visita = Visita(
                idCliente = selectedClienteId,
                idVendedor = idUsuario,
                fechaVisita = fechaISO,
                motivo = motivoSeleccionado,
                estado = "PENDIENTE"
            )

            registrarVisita(visita)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarClientesDesdeApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicio-cliente-596275467600.us-central1.run.app/api/")
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
                    Toast.makeText(this@RegistrarVisitaActivity,
                        getString(R.string.error_al_cargar_clientes), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RespuestaCliente>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@RegistrarVisitaActivity,
                    getString(R.string.error_de_conexi_n_con_clientes), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun obtenerZonaVendedor(idUsuario: String?) {
        if (idUsuario.isNullOrEmpty()) {
            Toast.makeText(this, getString(R.string.id_de_usuario_no_disponible), Toast.LENGTH_SHORT).show()
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://vendedor-596275467600.us-central1.run.app/api/")
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
                        showToast(getString(R.string.datos_de_vendedor_no_v_lidos))
                    }

                } else {
                    Toast.makeText(
                        this@RegistrarVisitaActivity,
                        getString(R.string.error_al_cargar_clientes),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<RespuestaVendedor>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(
                    this@RegistrarVisitaActivity,
                    R.string.error_de_conexi_n_con_clientes,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun showToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun validarCampos(): Boolean {
        val selectedSpinnerC = spinnerCliente.selectedItemPosition
        if (selectedSpinnerC <= 0 || listaClientes.isEmpty()) {
            Toast.makeText(this, "Por favor selecciona un cliente", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        val selectedSpinnerM = spinnerMotivo.selectedItem.toString()
        if (selectedSpinnerM == "Selecciona uno") {
            Toast.makeText(this, "Por favor selecciona un motivo", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        if (editFecha.text.toString().trim().isEmpty()) {
            showToast(getString(R.string.ingrese_la_fecha_de_entrega))
            return false
        }

        if (!validarFecha(editFecha.text.toString().trim())) {
            showToast(getString(R.string.la_fecha_de_visita_debe_tener_el_formato_yyyy_mm_dd))
            return false
        }

        try {
            val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            formato.isLenient = false
            val fechaIngresada = formato.parse(editFecha.text.toString())
            val fechaActual = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.time

            if (fechaIngresada.before(fechaActual)) {
                showToast("La fecha no puede ser anterior a la fecha actual")
                return false
            }
        } catch (e: Exception) {
            showToast("Error al procesar la fecha")
            return false
        }

        return true

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

    private fun registrarVisita(visita: Visita) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicio-visitas-596275467600.us-central1.run.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        Log.d("VISITA", Gson().toJson(visita))
        apiService.crearVisita(visita).enqueue(object : Callback<RespuestaRequest> {
            override fun onResponse(call: Call<RespuestaRequest>, response: Response<RespuestaRequest>) {
                if (response.isSuccessful) {
                    val respuesta = response.body()
                    Toast.makeText(this@RegistrarVisitaActivity,
                        getString(R.string.la_visita_ha_sido_registrada_exitosamente), Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@RegistrarVisitaActivity, MenuActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {

                    Toast.makeText(this@RegistrarVisitaActivity,
                        getString(R.string.no_fue_posible_crear_la_visita_intente_de_nuevo), Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<RespuestaRequest>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@RegistrarVisitaActivity,
                    getString(R.string.error_de_conexi_n_con_visita), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cambiarIdioma(context: Context, codigoIdioma: String): Context {
        val locale = Locale(codigoIdioma)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }

    private fun guardarIdioma(context: Context, idioma: String) {
        val prefs = context.getSharedPreferences("Ajustes", Context.MODE_PRIVATE)
        prefs.edit().putString("idioma", idioma).apply()
    }

    private fun obtenerIdiomaGuardado(context: Context): String {
        val prefs = context.getSharedPreferences("Ajustes", Context.MODE_PRIVATE)
        return prefs.getString("idioma", "es") ?: "es"
    }

    private fun recrearConNuevoIdioma(codigoIdioma: String) {
        val context = cambiarIdioma(this, codigoIdioma)
        val intent = intent
        finish()
        startActivity(intent)
    }

    private fun convertirFechaAISO8601(fecha: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        outputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date: Date = inputFormat.parse(fecha)!!
        return outputFormat.format(date)
    }

}