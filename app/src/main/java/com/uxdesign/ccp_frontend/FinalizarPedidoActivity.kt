package com.uxdesign.ccp_frontend

import android.content.Intent
import android.os.Bundle
import java.text.SimpleDateFormat
import java.util.Locale
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
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

class FinalizarPedidoActivity : AppCompatActivity() {
    private lateinit var spinnerCliente: Spinner
    private lateinit var editFecha: EditText
    private lateinit var editHora: EditText
    private lateinit var editNumProductos: EditText
    private lateinit var editTotal: EditText
    private lateinit var editComentarios: EditText
    private var listaClientes: List<Cliente> = emptyList()
    private var selectedClienteId: String = ""
    private lateinit var vendedor: Vendedor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_finalizar_pedido)

        val idUsuario = intent.getStringExtra("id_usuario")
        val cantidadProd = intent.getIntExtra("cantidad_productos", 0)
        val valorTotal = intent.getDoubleExtra("valor_total", 0.0)

        spinnerCliente = findViewById(R.id.spinnerCliente)
        editFecha = findViewById(R.id.editFechaEntrega)
        editHora = findViewById(R.id.editHora)
        editNumProductos = findViewById(R.id.editNumProductos)
        editTotal = findViewById(R.id.editTotal)
        editComentarios = findViewById(R.id.editComentarios)

        editNumProductos.setText(cantidadProd.toString())
        editTotal.setText("$${String.format("%.2f", valorTotal)}")


        //Adaptabilidad
        val mainLayout: ScrollView = findViewById(R.id.main)
        val titleCliente: TextView = findViewById(R.id.tituloCliente)
        val titleDate: TextView = findViewById(R.id.tituloFechaEntrega)
        val titleHora: TextView = findViewById(R.id.tituloHora)
        val titleCantidad: TextView = findViewById(R.id.tituloNumProductos)
        val titleTotal: TextView = findViewById(R.id.tituloTotal)
        val titleComentarios: TextView = findViewById(R.id.tituloComentarios)
        val buttonRegistrar: Button = findViewById(R.id.buttonRegistrar)
        val imageEye: ImageView = findViewById(R.id.imageOjoN)

        imageEye.visibility = View.GONE

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

            if (!validarCampos()) {
                return@setOnClickListener
            }

            val cliente = spinnerCliente.selectedItem.toString()
            val fechaEntrega = editFecha.text.toString().trim()
            val hora = editHora.text.toString().trim()
            val comentarios = editComentarios.text.toString().trim()
            //val numProductos = editNumProductos.text.toString().trim().toInt()
            //val total = editTotal.text.toString().trim().toDouble()

           val pedido = Pedido(
                idCliente = selectedClienteId,
                fechaEntrega = fechaEntrega,
                estadoPedido = "CREADO",
                valorTotal = valorTotal,
                idVendedor = idUsuario,
                comentarios = comentarios,
                idMoneda = 11
            )

            enviarPedido(pedido)
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
                        this@FinalizarPedidoActivity,
                        android.R.layout.simple_spinner_item,
                        listOf("Selecciona uno") + nombresClientes
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerCliente.adapter = adapter
                } else {
                    Toast.makeText(this@FinalizarPedidoActivity, "Error al cargar clientes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RespuestaCliente>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@FinalizarPedidoActivity, "Error de conexión con clientes", Toast.LENGTH_SHORT).show()
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
            override fun onResponse(call: Call<RespuestaVendedor>, response: Response<RespuestaVendedor>) {
                if (response.isSuccessful) {
                    val vendedorS = response.body()?.vendedor

                    if (vendedorS != null) {
                        vendedor = vendedorS
                        cargarClientesDesdeApi()
                    } else {
                        showToast("Datos de vendedor no válidos")
                    }

                } else {
                    Toast.makeText(this@FinalizarPedidoActivity, "Error al cargar clientes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RespuestaVendedor>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@FinalizarPedidoActivity, "Error de conexión con clientes", Toast.LENGTH_SHORT).show()
            }
        })
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

        if (!validarFecha(editFecha.text.toString().trim())) {
            showToast("La fecha de entrega debe tener el formato yyyy-MM-dd")
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

    private fun showToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun enviarPedido(pedido: Pedido) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicio-pedidos-596275467600.us-central1.run.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.crearPedido(pedido).enqueue(object : Callback<RespuestaRequestPedido> {
            override fun onResponse(call: Call<RespuestaRequestPedido>, response: Response<RespuestaRequestPedido>) {
                if (response.isSuccessful) {
                    val respuesta = response.body()
                    val idPedido = respuesta?.id
                    if (idPedido != null){
                        asociarDetalles(pedido.idCliente, idPedido)
                    } else {

                    Toast.makeText(this@FinalizarPedidoActivity, "No fue posible crear el pedido, intente de nuevo", Toast.LENGTH_SHORT).show()
                }

                } else {
                    Toast.makeText(this@FinalizarPedidoActivity, "Error al crear pedido", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RespuestaRequestPedido>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@FinalizarPedidoActivity, "Error de conexión con pedido", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun asociarDetalles(idUsuario: String, idPedido: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicio-pedidos-596275467600.us-central1.run.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.enlazarDetallePedido(idUsuario, idPedido).enqueue(object : Callback<RespuestaRequestPedido> {
            override fun onResponse(call: Call<RespuestaRequestPedido>, response: Response<RespuestaRequestPedido>) {
                if (response.isSuccessful) {
                    val respuesta = response.body()
                    val status = respuesta?.status
                    if (status == 201){
                        Toast.makeText(this@FinalizarPedidoActivity, "El pedido ha sido registrado exitosamente", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@FinalizarPedidoActivity, CatalogoProductosActivity::class.java)
                        startActivity(intent)

                        finish()
                    } else {

                        Toast.makeText(this@FinalizarPedidoActivity, "No fue posible agregar detalles, intente de nuevo", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this@FinalizarPedidoActivity, "Error al agregar detalles", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RespuestaRequestPedido>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@FinalizarPedidoActivity, "Error de conexión con pedido", Toast.LENGTH_SHORT).show()
            }
        })
    }
}