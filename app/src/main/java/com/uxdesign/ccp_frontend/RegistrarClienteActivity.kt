package com.uxdesign.ccp_frontend

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView

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
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrarClienteActivity : AppCompatActivity() {

    private lateinit var nombreText: EditText
    private lateinit var apellidoText: EditText
    private lateinit var contraseniaText: EditText
    private lateinit var numDocText: EditText
    private lateinit var telefonoText: EditText
    private lateinit var correoText: EditText
    private lateinit var direccionText: EditText
    private lateinit var buttonCrear: Button
    private lateinit var spinnerTipoDoc: Spinner
    private lateinit var spinnerCiudad: Spinner
    private lateinit var spinnerZona: Spinner
    private var listaCiudades: List<Ciudad> = emptyList()
    private var selectedCiudadId: String = ""
    private var listaZonas: List<Zona> = emptyList()
    private var selectedZonaId: String = ""
    private var listaTiposDocs: List<TipoDocumento> = emptyList()
    private var selectedTipoDoc: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_cliente)

        spinnerTipoDoc = findViewById<Spinner>(R.id.spinnerTipoDoc)
        spinnerCiudad = findViewById<Spinner>(R.id.spinnerCiudad)
        spinnerZona = findViewById<Spinner>(R.id.spinnerZona)
        nombreText = findViewById(R.id.editNombres)
        apellidoText = findViewById(R.id.editApellidos)
        contraseniaText = findViewById(R.id.editContrasenia)
        numDocText = findViewById(R.id.editDocumento)
        telefonoText = findViewById(R.id.editTelefono)
        correoText = findViewById(R.id.editMail)
        direccionText = findViewById(R.id.editDireccion)
        buttonCrear = findViewById(R.id.buttonCrearCliente)

        cargarCiudadesDesdeApi()
        cargarTiposDocDesdeApi()

        spinnerCiudad.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (position == 0 ){
                    return
                }

                val ciudadSeleccionada = listaCiudades[position - 1]
                selectedCiudadId = ciudadSeleccionada.id

                cargarZonasDesdeApi(selectedCiudadId)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // No hacer nada
            }
        }

        buttonCrear.setOnClickListener {
            if (!validarCampos()) {
                return@setOnClickListener
            }

            val posicionTipoDocumento = spinnerTipoDoc.selectedItemPosition
            val posicionCiudad = spinnerCiudad.selectedItemPosition
            val posicionZona = spinnerZona.selectedItemPosition

            val ciudadSeleccionado = listaCiudades[posicionCiudad - 1]
            val idCiudad = ciudadSeleccionado.id
            selectedCiudadId = idCiudad

            val zonaSeleccionado = listaZonas[posicionZona - 1]
            val idZona = zonaSeleccionado.id
            selectedZonaId = idZona

            val tipoDocSeleccionado = listaTiposDocs[posicionTipoDocumento - 1]
            val idTipoDoc = tipoDocSeleccionado.codigo
            selectedTipoDoc = idTipoDoc

            val cliente = Cliente(
                id = "",
                nombre = nombreText.text.toString(),
                apellido = apellidoText.text.toString(),
                documento = numDocText.text.toString(),
                tipoDocumento = selectedTipoDoc,
                telefono = telefonoText.text.toString(),
                email = correoText.text.toString(),
                direccion = direccionText.text.toString(),
                idCiudad = selectedCiudadId,
                idZona = selectedZonaId
                //contrasenia = contraseniaText.text.toString()
            )

            val retrofit = Retrofit.Builder()
                .baseUrl("https://servicio-cliente-596275467600.us-central1.run.app/api/")  // Aqui URL de microservicio
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val apiService = retrofit.create(ApiService::class.java)
            apiService.registrarCliente(cliente).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val dialog = android.app.AlertDialog.Builder(this@RegistrarClienteActivity)
                            .setTitle("Registro exitoso")
                            .setMessage("El cliente ha sido registrado correctamente.")
                            .setPositiveButton("Aceptar") { dialogInterface, _ ->
                                val intent =
                                    Intent(this@RegistrarClienteActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .create()

                        dialog.show()
                    } else {
                        Toast.makeText(
                            this@RegistrarClienteActivity,
                            "Error: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(
                        this@RegistrarClienteActivity,
                        "Fallo: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })

        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarCiudadesDesdeApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicio-atributos-596275467600.us-central1.run.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getCiudades().enqueue(object : Callback<RespuestaCiudad> {
            override fun onResponse(call: Call<RespuestaCiudad>, response: Response<RespuestaCiudad>) {
                if (response.isSuccessful) {
                    val ciudades = response.body()?.ciudades ?: emptyList()

                    listaCiudades = ciudades

                    val nombresCiudades = ciudades.map { "${it.nombre}" }
                    val adapter = ArrayAdapter(
                        this@RegistrarClienteActivity,
                        android.R.layout.simple_spinner_item,
                        listOf("Selecciona una") + nombresCiudades
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerCiudad.adapter = adapter
                } else {
                    Toast.makeText(this@RegistrarClienteActivity, "Error al cargar ciudades", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RespuestaCiudad>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@RegistrarClienteActivity, "Error de conexión en ciudades", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cargarZonasDesdeApi(ciudadId: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicio-atributos-596275467600.us-central1.run.app/api/") // Cambia por tu URL real
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.getZonasPorCiudad(ciudadId).enqueue(object : Callback<RespuestaZona> {
            override fun onResponse(call: Call<RespuestaZona>, response: Response<RespuestaZona>) {
                if (response.isSuccessful) {
                    listaZonas = response.body()?.zonas ?: emptyList()

                    val nombresZonas = listaZonas.map { it.nombre }
                    val adapter = ArrayAdapter(
                        this@RegistrarClienteActivity,
                        android.R.layout.simple_spinner_item,
                        listOf("Selecciona una") + nombresZonas
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerZona.adapter = adapter
                } else {
                    Toast.makeText(this@RegistrarClienteActivity, "Error al cargar las zonas", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RespuestaZona>, t: Throwable) {
                Toast.makeText(this@RegistrarClienteActivity, "Error de conexión de zonas", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cargarTiposDocDesdeApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://proveedores-596275467600.us-central1.run.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getTiposDocumento().enqueue(object : Callback<RespuestaTiposDocs> {
            override fun onResponse(call: Call<RespuestaTiposDocs>, response: Response<RespuestaTiposDocs>) {
                if (response.isSuccessful) {
                    val tiposDocs = response.body()?.documentos ?: emptyList()

                    listaTiposDocs = tiposDocs

                    val nombresTiposDocs = tiposDocs.map { "${it.nombre}" }
                    val adapter = ArrayAdapter(
                        this@RegistrarClienteActivity,
                        android.R.layout.simple_spinner_item,
                        listOf("Selecciona uno") + nombresTiposDocs
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerTipoDoc.adapter = adapter
                } else {
                    Toast.makeText(this@RegistrarClienteActivity, "Error al cargar tipos de documento", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RespuestaTiposDocs>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@RegistrarClienteActivity, "Error de conexión en tipos de documento", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun validarCampos(): Boolean {
        val nombre = nombreText.text.toString()
        if (nombre.isEmpty()) {
            nombreText.error = "El nombre no puede estar vacío"
            return false
        }

        val apellido = apellidoText.text.toString()
        if (apellido.isEmpty()) {
            apellidoText.error = "El apellido no puede estar vacío"
            return false
        }

        val documento = numDocText.text.toString()
        if (documento.isEmpty()) {
            numDocText.error = "El número de documento no puede estar vacío"
            return false
        }

        val contrasenia = contraseniaText.text.toString()
        if (contrasenia.isEmpty()) {
            contraseniaText.error = "La contraseña no puede estar vacía"
            return false
        }

        val direccion = direccionText.text.toString()
        if (direccion.isEmpty()) {
            direccionText.error = "La dirección no puede estar vacía"
            return false
        }

        val email = correoText.text.toString()
        if (email.isEmpty()) {
            correoText.error = "El correo no puede estar vacío"
            return false
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$".toRegex())) {
            correoText.error = "Por favor ingresa un correo electrónico válido"
            return false
        }

        val telefono = telefonoText.text.toString()
        if (telefono.isEmpty()) {
            telefonoText.error = "El teléfono no puede estar vacío"
            return false
        } else if (telefono.length < 7) {
            telefonoText.error = "Por favor ingresa un número de teléfono válido"
            return false
        }

        val selectedSpinnerTD = spinnerTipoDoc.selectedItem.toString()
        if (selectedSpinnerTD == "Selecciona uno") {
            Toast.makeText(this, "Por favor selecciona un tipo de documento", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        val selectedSpinnerC = spinnerCiudad.selectedItem.toString()
        if (selectedSpinnerC == "Selecciona uno") {
            Toast.makeText(this, "Por favor selecciona una ciudad", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        val selectedSpinnerZ = spinnerZona.selectedItem.toString()
        if (selectedSpinnerZ == "Selecciona uno") {
            Toast.makeText(this, "Por favor selecciona una zona", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        return true
    }
}