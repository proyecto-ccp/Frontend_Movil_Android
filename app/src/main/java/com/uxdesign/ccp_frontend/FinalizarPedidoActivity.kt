package com.uxdesign.ccp_frontend

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import java.text.SimpleDateFormat
import java.util.Locale
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
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.ParseException
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

class FinalizarPedidoActivity : AppCompatActivity() {
    private lateinit var spinnerCliente: Spinner
    private lateinit var editFecha: EditText
    private lateinit var editNumProductos: EditText
    private lateinit var editTotal: EditText
    private lateinit var editComentarios: EditText
    private var listaClientes: List<Cliente> = emptyList()
    private var selectedClienteId: String = ""
    private lateinit var vendedor: Vendedor
    private lateinit var idUsuario: String
    private val DATE_FORMAT = "yyyy-MM-dd"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_finalizar_pedido)

        idUsuario = intent.getStringExtra("id_usuario") ?: ""
        val cantidadProd = intent.getIntExtra("cantidad_productos", 0)
        val valorTotal = intent.getDoubleExtra("valor_total", 0.0)

        spinnerCliente = findViewById(R.id.spinnerCliente)
        editFecha = findViewById(R.id.editFechaEntrega)

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

        editNumProductos = findViewById(R.id.editNumProductos)
        editTotal = findViewById(R.id.editTotal)
        editComentarios = findViewById(R.id.editComentarios)

        editNumProductos.setText(cantidadProd.toString())
        editTotal.setText("$${String.format("%.2f", valorTotal)}")

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

            if (!validarCampos()) {
                return@setOnClickListener
            }

            val fechaEntrega = editFecha.text.toString().trim()
            val fechaISO = convertirFechaAISO8601(fechaEntrega)
            val comentarios = editComentarios.text.toString().trim()

            val pedido = Pedido(
                idCliente = selectedClienteId,
                fechaEntrega = fechaISO,
                estadoPedido = "CREADO",
                valorTotal = valorTotal,
                idVendedor = idUsuario,
                comentarios = comentarios,
                idMoneda = 11
            )

            enviarPedido(pedido, idUsuario)
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
            .baseUrl("https://vendedor-596275467600.us-central1.run.app/api/")
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

        if (editNumProductos.text.toString().trim().isEmpty()) {
            showToast("Número de productos es obligatorio")
            return false
        }

        if (editTotal.text.toString().trim().isEmpty()) {
            showToast("Total del pedido es obligatorio")
            return false
        }

        try {
            val formato = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
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
        val formato = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

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

    private fun enviarPedido(pedido: Pedido, idUsuario: String) {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(this))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicio-pedidos-596275467600.us-central1.run.app/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.crearPedido(pedido).enqueue(object : Callback<RespuestaRequest> {
            override fun onResponse(call: Call<RespuestaRequest>, response: Response<RespuestaRequest>) {
                if (response.isSuccessful) {
                    val respuesta = response.body()
                    val idPedido = respuesta?.id
                    if (idPedido != null){
                        asociarDetalles(idUsuario, idPedido)
                    } else {

                        Toast.makeText(this@FinalizarPedidoActivity, "No fue posible crear el pedido, intente de nuevo", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this@FinalizarPedidoActivity, "Error al crear pedido", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RespuestaRequest>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@FinalizarPedidoActivity, "Error de conexión con pedido", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun asociarDetalles(idUsuario: String, idPedido: String) {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(this))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicio-pedidos-596275467600.us-central1.run.app/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.enlazarDetallePedido(idUsuario, idPedido).enqueue(object : Callback<RespuestaRequest> {
            override fun onResponse(call: Call<RespuestaRequest>, response: Response<RespuestaRequest>) {
                if (response.isSuccessful) {
                    val respuesta = response.body()
                    val status = respuesta?.status
                    if (status == 201){
                        Toast.makeText(this@FinalizarPedidoActivity, "El pedido ha sido registrado exitosamente", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@FinalizarPedidoActivity, CatalogoProductosActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    } else {

                        Toast.makeText(this@FinalizarPedidoActivity, "No fue posible agregar detalles, intente de nuevo", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this@FinalizarPedidoActivity, "Error al agregar detalles", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RespuestaRequest>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@FinalizarPedidoActivity, "Error de conexión con pedido", Toast.LENGTH_SHORT).show()
            }

        })

    }

    private fun convertirFechaAISO8601(fecha: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        outputFormat.timeZone = TimeZone.getTimeZone("UTC")

        val date: Date = inputFormat.parse(fecha)!!
        return outputFormat.format(date)
    }
}