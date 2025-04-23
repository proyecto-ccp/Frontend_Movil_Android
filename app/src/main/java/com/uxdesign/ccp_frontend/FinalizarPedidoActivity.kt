package com.uxdesign.ccp_frontend

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uxdesign.cpp.R

class FinalizarPedidoActivity : AppCompatActivity() {
    private lateinit var spinnerCliente: Spinner
    private lateinit var editFecha: EditText
    private lateinit var editHora: EditText
    private lateinit var editNumProductos: EditText
    private lateinit var editTotal: EditText
    private lateinit var editComentarios: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_finalizar_pedido)

        spinnerCliente = findViewById(R.id.spinnerCliente)
        editFecha = findViewById(R.id.editFechaEntrega)
        editHora = findViewById(R.id.editHora)
        editNumProductos = findViewById(R.id.editNumProductos)
        editTotal = findViewById(R.id.editTotal)
        editComentarios = findViewById(R.id.editComentarios)

        //Adaptabilidad
        val mainLayout: ConstraintLayout = findViewById(R.id.main)
        val titleCliente: TextView = findViewById(R.id.tituloCliente)
        val titleDate: TextView = findViewById(R.id.tituloFechaEntrega)
        val titleHora: TextView = findViewById(R.id.tituloHora)
        val titleCantidad: TextView = findViewById(R.id.tituloNumProductos)
        val titleTotal: TextView = findViewById(R.id.tituloTotal)
        val titleComentarios: TextView = findViewById(R.id.tituloComentarios)
        val buttonRegistrar: Button = findViewById(R.id.buttonRegistrar)
        val imageEye: ImageView = findViewById(R.id.imageOjoN)

        imageEye.visibility = View.VISIBLE

        imageEye.setOnClickListener {


        //val buttonOjo: Button = findViewById(R.id.botonOjo)
        //buttonOjo.setOnClickListener {
        // mainLayout.setBackgroundColor(resources.getColor(R.color.darkgrey, null))
        // titleCliente.setTextColor(resources.getColor(R.color.greytext, null))
        // titleDate.setTextColor(resources.getColor(R.color.greytext, null))
        // titleHora.setTextColor(resources.getColor(R.color.greytext, null))
        // titleCantidad.setTextColor(resources.getColor(R.color.greytext, null))
        // titleTotal.setTextColor(resources.getColor(R.color.greytext, null))
        // titleComentarios.setTextColor(resources.getColor(R.color.greytext, null))
        // buttonRegistrar.setBackgroundColor(resources.getColor(R.color.greytext, null))
            imageEye.setImageResource(R.drawable.blackeye)
            imageEye.visibility = View.GONE

        }

        //User interface
        // mainLayout.setBackgroundColor(resources.getColor(R.color.orange, null))
        // titleCliente.setTextColor(resources.getColor(R.color.pink, null))
        // titleDate.setTextColor(resources.getColor(R.color.pink, null))
        // titleHora.setTextColor(resources.getColor(R.color.pink, null))
        // titleCantidad.setTextColor(resources.getColor(R.color.pink, null))
        // titleTotal.setTextColor(resources.getColor(R.color.pink, null))
        // titleComentarios.setTextColor(resources.getColor(R.color.pink, null))

        //--------------------------------------

        buttonRegistrar.setOnClickListener {
            if (!validarCampos()) {
                return@setOnClickListener
            }

            val cliente = spinnerCliente.selectedItem.toString()
            val fechaEntrega = editFecha.text.toString().trim()
            val hora = editHora.text.toString().trim()
            val comentarios = editComentarios.text.toString().trim()
            val numProductos = editNumProductos.text.toString().trim().toInt()
            val total = editTotal.text.toString().trim().toDouble()

            val pedido = mapOf(
                "cliente" to cliente,
                "fechaEntrega" to fechaEntrega,
                "hora" to hora,
                "comentarios" to comentarios,
                "numeroProductos" to numProductos,
                "total" to total
            )

            enviarPedido(pedido)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun validarCampos(): Boolean {
        if (spinnerCliente.selectedItem == null || spinnerCliente.selectedItem.toString().isEmpty()) {
            showToast("Seleccione un cliente")
            return false
        }

        if (editFecha.text.toString().trim().isEmpty()) {
            showToast("Ingrese la fecha de entrega")
            return false
        }

        if (editHora.text.toString().trim().isEmpty()) {
            showToast("Ingrese la hora")
            return false
        }

        if (editNumProductos.text.toString().trim().isEmpty()) {
            showToast("Número de productos es obligatorio")
            return false
        }

        if (editTotal.text.toString().trim().isEmpty()) {
            showToast("Total del pedido es obligatorio")
            return false
        }

        return true

    }

    private fun showToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun enviarPedido(pedido: Map<String, Any>) {
        // Aquí deberías hacer tu llamada al microservicio (ej: Retrofit)
        // Simulación de envío
        Toast.makeText(this, "Enviando pedido...", Toast.LENGTH_SHORT).show()

        // TODO: implementar la llamada real al microservicio
    }
}