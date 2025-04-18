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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_cliente)

        spinnerTipoDoc = findViewById<Spinner>(R.id.spinnerTipoDoc)
        nombreText = findViewById(R.id.editNombres)
        apellidoText = findViewById(R.id.editApellidos)
        contraseniaText = findViewById(R.id.editContrasenia)
        numDocText = findViewById(R.id.editDocumento)
        telefonoText = findViewById(R.id.editTelefono)
        correoText = findViewById(R.id.editMail)
        direccionText = findViewById(R.id.editDireccion)
        buttonCrear = findViewById(R.id.buttonCrearCliente)

        val tipo_docs = resources.getStringArray(R.array.tipos_doc)
        val valores = resources.getStringArray(R.array.values_doc)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipo_docs)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoDoc.adapter = adapter

        buttonCrear.setOnClickListener {
            if (!validarCampos()) {
                return@setOnClickListener
            }

            val tipoDocumento = valores[spinnerTipoDoc.selectedItemPosition]

            val cliente = Cliente(
                id = "",
                nombre = nombreText.text.toString(),
                apellido = apellidoText.text.toString(),
                documento = numDocText.text.toString(),
                tipoDocumento = tipoDocumento,
                telefono = telefonoText.text.toString(),
                email = correoText.text.toString(),
                direccion = direccionText.text.toString(),
                //contrasenia = contraseniaText.text.toString()
            )

            val retrofit = Retrofit.Builder()
                .baseUrl("https://servicio-cliente-596275467600.us-central1.run.app/api/Cliente/CrearCliente")  // Aqui URL de microservicio
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

        val selectedSpinner = spinnerTipoDoc.selectedItem.toString()
        if (selectedSpinner == "Selecciona uno") {
            Toast.makeText(this, "Por favor selecciona un tipo de documento", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        return true
    }
}