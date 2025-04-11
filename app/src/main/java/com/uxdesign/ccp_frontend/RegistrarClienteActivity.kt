package com.uxdesign.ccp_frontend

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

class RegistrarClienteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registrar_cliente)

        val spinnerTipoDoc = findViewById<Spinner>(R.id.spinnerTipoDoc)
        val nombreText: EditText = findViewById(R.id.editNombres)
        val apellidoText: EditText = findViewById(R.id.editApellidos)
        val contraseniaText: EditText = findViewById(R.id.editContrasenia)
        val numDocText: EditText = findViewById(R.id.editDocumento)
        val telefonoText: EditText = findViewById(R.id.editTelefono)
        val correoText: EditText = findViewById(R.id.editMail)
        val direccionText: EditText = findViewById(R.id.editDireccion)
        val buttonCrear : Button = findViewById(R.id.buttonCrearCliente)

        val tipo_docs = resources.getStringArray(R.array.tipos_doc)
        val valores = resources.getStringArray(R.array.values_doc)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipo_docs)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoDoc.adapter = adapter

        buttonCrear.setOnClickListener {
            val nombre = nombreText.text.toString()
            if (nombre.isEmpty()) {
                nombreText.error = "El nombre no puede estar vacío"
                return@setOnClickListener
            }

            val apellido = apellidoText.text.toString()
            if (apellido.isEmpty()) {
                apellidoText.error = "El apellido no puede estar vacío"
                return@setOnClickListener
            }

            val documento = numDocText.text.toString()
            if (documento.isEmpty()) {
                numDocText.error = "El número de documento no puede estar vacío"
                return@setOnClickListener
            }

            val contrasenia = contraseniaText.text.toString()
            if (contrasenia.isEmpty()) {
                contraseniaText.error = "La contraseña no puede estar vacía"
                return@setOnClickListener
            }

            val direccion = direccionText.text.toString()
            if (direccion.isEmpty()) {
                direccionText.error = "La dirección no puede estar vacía"
                return@setOnClickListener
            }

            val email = correoText.text.toString()
            if (email.isEmpty()){
                correoText.error = "El correo no puede estar vacío"
                return@setOnClickListener
            } else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$".toRegex())){
                correoText.error = "Por favor ingresa un correo electrónico válido"
                return@setOnClickListener
            }

            val telefono = telefonoText.text.toString()
            if (telefono.isEmpty()) {
                telefonoText.error = "El teléfono no puede estar vacío"
                return@setOnClickListener
            } else if (telefono.length < 7) {
                telefonoText.error = "Por favor ingresa un número de teléfono válido"
                return@setOnClickListener
            }

            val selectedSpinner = spinnerTipoDoc.selectedItem.toString()
            if (selectedSpinner == "Selecciona uno") {
                Toast.makeText(this, "Por favor selecciona un tipo de documento", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //genera json y llama a microservicio

        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}