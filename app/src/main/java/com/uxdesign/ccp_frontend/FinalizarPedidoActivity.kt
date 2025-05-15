package com.uxdesign.ccp_frontend

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uxdesign.cpp.R
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class FinalizarPedidoActivity : AppCompatActivity() {

    private lateinit var spinnerCliente: Spinner
    private lateinit var editFecha: EditText
    private lateinit var editNumProductos: EditText
    private lateinit var editTotal: EditText
    private lateinit var editComentarios: EditText
    private lateinit var pedidoLogic: PedidoLogic

    private var listaClientes: List<Cliente> = emptyList()
    private var selectedClienteId: String = ""
    private lateinit var vendedor: Vendedor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finalizar_pedido)

        // Inicializar Retrofit y lógica
        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicio-backend/api/") // Cambia por base común si unificaste
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        pedidoLogic = PedidoLogic(apiService)

        // Obtener datos del intent
        val idUsuario = intent.getStringExtra("id_usuario")
        val cantidadProd = intent.getIntExtra("cantidad_productos", 0)
        val valorTotal = intent.getDoubleExtra("valor_total", 0.0)

        // Inicializar UI
        spinnerCliente = findViewById(R.id.spinnerCliente)
        editFecha = findViewById(R.id.editFechaEntrega)
        editNumProductos = findViewById(R.id.editNumProductos)
        editTotal = findViewById(R.id.editTotal)
        editComentarios = findViewById(R.id.editComentarios)
        val buttonRegistrar: Button = findViewById(R.id.buttonRegistrar)

        editNumProductos.setText(cantidadProd.toString())
        editTotal.setText("$${String.format("%.2f", valorTotal)}")

        editFecha.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val fechaSeleccionada = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                editFecha.setText(fechaSeleccionada)
            }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]).show()
        }

        buttonRegistrar.setOnClickListener {
            if (idUsuario.isNullOrEmpty()) {
                showToast("ID de usuario no disponible")
                return@setOnClickListener
            }

            val clientePos = spinnerCliente.selectedItemPosition
            if (clientePos <= 0) {
                showToast("Seleccione un cliente")
                return@setOnClickListener
            }

            val clienteSeleccionado = listaClientes[clientePos - 1]
            selectedClienteId = clienteSeleccionado.id

            val fecha = editFecha.text.toString()
            val numProductos = editNumProductos.text.toString()
            val total = editTotal.text.toString()

            if (!pedidoLogic.validarCampos(fecha, numProductos, total, clientePos)) {
                showToast("Complete correctamente todos los campos")
                return@setOnClickListener
            }

            val pedido = Pedido(
                idCliente = selectedClienteId,
                fechaEntrega = pedidoLogic.convertirFechaAISO8601(fecha),
                estadoPedido = "CREADO",
                valorTotal = valorTotal,
                idVendedor = idUsuario,
                comentarios = editComentarios.text.toString(),
                idMoneda = 11
            )

            enviarPedido(pedido, idUsuario)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (!idUsuario.isNullOrEmpty()) {
            obtenerZonaVendedor(idUsuario)
        }
    }

    private fun obtenerZonaVendedor(idUsuario: String) {
        pedidoLogic.obtenerVendedor(idUsuario, object : Callback<RespuestaVendedor> {
            override fun onResponse(call: Call<RespuestaVendedor>, response: Response<RespuestaVendedor>) {
                if (response.isSuccessful) {
                    val vendedorRes = response.body()?.vendedor
                    if (vendedorRes != null) {
                        vendedor = vendedorRes
                        cargarClientes(vendedor.idzona)
                    } else {
                        showToast("Vendedor no válido")
                    }
                } else {
                    showToast("Error al obtener vendedor")
                }
            }

            override fun onFailure(call: Call<RespuestaVendedor>, t: Throwable) {
                t.printStackTrace()
                showToast("Fallo de conexión al obtener vendedor")
            }
        })
    }

    private fun cargarClientes(idZona: String) {
        pedidoLogic.obtenerClientesPorZona(idZona, object : Callback<RespuestaCliente> {
            override fun onResponse(call: Call<RespuestaCliente>, response: Response<RespuestaCliente>) {
                if (response.isSuccessful) {
                    listaClientes = response.body()?.clientes ?: emptyList()
                    val nombres = listaClientes.map { "${it.nombre} ${it.apellido}" }
                    val adapter = ArrayAdapter(
                        this@FinalizarPedidoActivity,
                        android.R.layout.simple_spinner_item,
                        listOf("Selecciona uno") + nombres
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerCliente.adapter = adapter
                } else {
                    showToast("Error al cargar clientes")
                }
            }

            override fun onFailure(call: Call<RespuestaCliente>, t: Throwable) {
                t.printStackTrace()
                showToast("Fallo de conexión al cargar clientes")
            }
        })
    }

    private fun enviarPedido(pedido: Pedido, idUsuario: String) {
        pedidoLogic.crearPedido(pedido, object : Callback<RespuestaRequest> {
            override fun onResponse(call: Call<RespuestaRequest>, response: Response<RespuestaRequest>) {
                if (response.isSuccessful) {
                    val idPedido = response.body()?.id
                    if (idPedido != null) {
                        asociarDetalles(idUsuario, idPedido)
                    } else {
                        showToast("Pedido creado, pero no se obtuvo ID")
                    }
                } else {
                    showToast("Error al crear pedido")
                }
            }

            override fun onFailure(call: Call<RespuestaRequest>, t: Throwable) {
                t.printStackTrace()
                showToast("Fallo de conexión al crear pedido")
            }
        })
    }

    private fun asociarDetalles(idUsuario: String, idPedido: String) {
        pedidoLogic.asociarDetalles(idUsuario, idPedido, object : Callback<RespuestaRequest> {
            override fun onResponse(call: Call<RespuestaRequest>, response: Response<RespuestaRequest>) {
                if (response.isSuccessful && response.body()?.status == 201) {
                    showToast("Pedido registrado exitosamente")
                    val intent = Intent(this@FinalizarPedidoActivity, CatalogoProductosActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    showToast("No fue posible asociar los detalles")
                }
            }

            override fun onFailure(call: Call<RespuestaRequest>, t: Throwable) {
                t.printStackTrace()
                showToast("Fallo de conexión al asociar detalles")
            }
        })
    }

    private fun showToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}
