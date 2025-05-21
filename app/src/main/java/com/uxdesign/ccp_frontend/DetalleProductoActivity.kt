package com.uxdesign.ccp_frontend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.uxdesign.cpp.R
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class DetalleProductoActivity : AppCompatActivity() {
    private lateinit var idUsuario: String
    private var productoPrecio: Double = 0.0
    private var stockDisponible: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_producto)

        val editCantidad: EditText = findViewById(R.id.editCantidad)
        val editValor: EditText = findViewById(R.id.editValor)
        val agregarButton: Button = findViewById(R.id.buttonAgregar)
        val imageProducto: ImageView = findViewById(R.id.imageProducto)
        val nombreProducto: TextView = findViewById(R.id.textNombreProducto)
        val precioProducto: TextView = findViewById(R.id.textPrecioProducto)
        val descripcionProducto: TextView = findViewById(R.id.textDescripcionProducto)
        val stockProducto: TextView = findViewById(R.id.textStock)

        val productoId = intent.getIntExtra("producto_id", -1)
        val productoNombre = intent.getStringExtra("producto_nombre")
        productoPrecio = intent.getDoubleExtra("producto_precio", 0.0)
        val productoDescripcion = intent.getStringExtra("producto_descripcion")
        val productoImagen = intent.getStringExtra("producto_imagen")
        idUsuario = intent.getStringExtra("id_usuario") ?: "nada"
        Log.d("USUSARIO_DETALLE", idUsuario)

        nombreProducto.text = productoNombre
        precioProducto.text = "$${productoPrecio}"
        descripcionProducto.text = productoDescripcion

        Glide.with(this)
            .load(productoImagen)
            .placeholder(R.drawable.errorphotopeque)
            .error(R.drawable.errorphotopeque)
            .into(imageProducto)


        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(this))
            .build()

        val retrofitStock = Retrofit.Builder()
            .baseUrl("https://inventarios-596275467600.us-central1.run.app/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiStock = retrofitStock.create(ApiService::class.java)
        val stockManager = StockManager(apiStock)

        val retrofitCarrito = Retrofit.Builder()
            .baseUrl("https://servicio-pedidos-596275467600.us-central1.run.app/api/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofitCarrito.create(ApiService::class.java)
        val productoManager = ProductoManager(apiService)

        stockManager.obtenerStock(productoId, object : StockManager.StockCallback {
            override fun onStockRecibido(stock: Int) {
                stockDisponible = stock
                stockProducto.text = "Stock: $stock unidades"

                if (stock <= 0) {
                    agregarButton.isEnabled = false
                    agregarButton.alpha = 0.5f
                    editCantidad.isEnabled = false
                }
            }

            override fun onError(mensaje: String) {
                Toast.makeText(this@DetalleProductoActivity, mensaje, Toast.LENGTH_SHORT).show()
                agregarButton.isEnabled = false
                agregarButton.alpha = 0.5f
                editCantidad.isEnabled = false
            }
        })

        editCantidad.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //Implementacion de caso  que no es necesario por eso queda vacio
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val cantidad = s.toString().toIntOrNull() ?: 0
                val total = cantidad * productoPrecio
                editValor.setText("$%.2f".format(total))
            }
            override fun afterTextChanged(s: android.text.Editable?) {
                //Implementacion de caso  que no es necesario por eso queda vacio
            }
        })

        agregarButton.setOnClickListener {
            val cantidadText = editCantidad.text.toString().trim()

            if (cantidadText.isEmpty() || cantidadText == "0") {
                Toast.makeText(this, "Por favor ingrese una cantidad válida", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val cantidad = cantidadText.toInt()

            if (cantidad > stockDisponible) {
                Toast.makeText(this, "La cantidad ingresada excede el stock disponible ($stockDisponible)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val producto = ProductoCarrito(
                idProducto = productoId,
                cantidad = cantidad,
                idUsuario = idUsuario,
                precioUnitario = productoPrecio
            )
            productoManager.agregarProducto(producto, object : ProductoManager.AgregarProductoCallback {
                override fun onAgregado() {
                    Toast.makeText(this@DetalleProductoActivity, "Producto agregado al carrito", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@DetalleProductoActivity, VerPedidoActivity::class.java)
                    intent.putExtra("id_usuario", idUsuario)
                    startActivity(intent)
                }

                override fun onError(mensaje: String) {
                    Toast.makeText(this@DetalleProductoActivity, mensaje, Toast.LENGTH_SHORT).show()
                }
            })
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}