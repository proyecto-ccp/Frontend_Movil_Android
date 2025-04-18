package com.uxdesign.ccp_frontend

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uxdesign.cpp.R
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.IOException

class CargarVideoActivity : AppCompatActivity() {
    private var listaClientes: List<Cliente> = emptyList()
    private var selectedClienteId: String = ""

    private val REQUEST_VIDEO_CAPTURE = 1
    private val REQUEST_VIDEO_GALLERY = 2
    private lateinit var videoBytes: ByteArray
    private lateinit var spinner: Spinner
    private lateinit var spinnerC: Spinner
    private lateinit var editTextNombreVideo: EditText
    private var nombreVideo: String = ""
    private lateinit var loadingContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cargar_video)

        spinner = findViewById(R.id.spinnerProductos)
        spinnerC = findViewById(R.id.spinnerClientes)
        editTextNombreVideo = findViewById(R.id.editNombres)

        val productos = listOf("Selecciona uno", 1, 2, 3)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, productos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        cargarClientesDesdeApi()
        //val adapterc = ArrayAdapter(this, android.R.layout.simple_spinner_item, clientes)
        //adapterc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //spinnerC.adapter = adapterc

        val buttonCargar = findViewById<Button>(R.id.buttonCargar)
        val buttonGaleria = findViewById<Button>(R.id.buttonGaleria)
        loadingContainer = findViewById(R.id.loadingContainer)

        buttonCargar.setOnClickListener {
            val productoSeleccionado = spinner.selectedItem.toString()
            val posicion = spinnerC.selectedItemPosition

            if (productoSeleccionado == "Selecciona uno" || posicion == 0) {
                Toast.makeText(this, "Debes seleccionar un producto y un cliente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val clienteSeleccionado = listaClientes[posicion - 1]
            val idCliente = clienteSeleccionado.id
            selectedClienteId = idCliente
            nombreVideo = editTextNombreVideo.text.toString()

            val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)

        }

       
        buttonGaleria.setOnClickListener {
            val productoSeleccionado = spinner.selectedItem.toString()
            val posicion = spinnerC.selectedItemPosition

            if (productoSeleccionado == "Selecciona uno" || posicion == 0) {
                Toast.makeText(this, "Debes seleccionar un producto y un cliente", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val clienteSeleccionado = listaClientes[posicion - 1]
            val idCliente = clienteSeleccionado.id

            nombreVideo = editTextNombreVideo.text.toString()

            val pickVideoIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            pickVideoIntent.type = "video/*"
            selectedClienteId = idCliente
            startActivityForResult(pickVideoIntent, REQUEST_VIDEO_GALLERY)

        }

        val buttonVerLista: Button = findViewById<Button>(R.id.buttonLista)
        buttonVerLista.setOnClickListener {
            val posicion = spinnerC.selectedItemPosition

            if (posicion > 0) {
                val clienteSeleccionado = listaClientes[posicion - 1]
                val idCliente = clienteSeleccionado.id

                val intent = Intent(this, ListaVideosActivity::class.java)
                intent.putExtra("CLIENTE_ID", idCliente)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Debes seleccionar un cliente de la lista", Toast.LENGTH_SHORT).show()
            }

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            when (requestCode) {
                REQUEST_VIDEO_CAPTURE -> {
                    // Lógica para el video capturado con la cámara
                    val videoUri = data?.data
                    if (videoUri != null) {
                        try {
                            val inputStream: InputStream = contentResolver.openInputStream(videoUri)!!
                            videoBytes = inputStream.readBytes()
                            saveVideoToFile(videoBytes)
                            sendVideo64ToServer(videoBytes, spinner.selectedItem.toString(), selectedClienteId)
                            videoBytes = ByteArray(0) // Liberar la memoria
                            spinner.setSelection(0)    // Limpiar el spinner
                            spinnerC.setSelection(0)
                            editTextNombreVideo.text.clear()
                        } catch (e: IOException) {
                            e.printStackTrace()
                            Toast.makeText(this, "Error al leer el video", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "No se pudo obtener el video", Toast.LENGTH_SHORT).show()
                    }
                }

                REQUEST_VIDEO_GALLERY -> {
                    // Lógica para el video seleccionado desde la galería
                    val videoUri = data?.data
                    if (videoUri != null) {
                        try {
                            val inputStream: InputStream = contentResolver.openInputStream(videoUri)!!
                            videoBytes = inputStream.readBytes()
                            //saveVideoToFile(videoBytes)
                                // sendVideoToServer(videoBytes, spinner.selectedItem.toString(), spinnerC.selectedItem.toString(), videoName)
                            sendVideo64ToServer(videoBytes, spinner.selectedItem.toString(), spinnerC.selectedItem.toString())
                            videoBytes = ByteArray(0) // Liberar la memoria
                            spinner.setSelection(0)    // Limpiar el spinner
                            spinnerC.setSelection(0)
                            editTextNombreVideo.text.clear()

                        } catch (e: IOException) {
                            e.printStackTrace()
                            Toast.makeText(this, "Error al leer el video", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "No se pudo obtener el video", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }    }


    private fun saveVideoToFile(videoBytes: ByteArray) {
        try {
            val file = File(filesDir, nombreVideo)
            val outputStream = FileOutputStream(file)

            outputStream.write(videoBytes)
            outputStream.close()

            //Toast.makeText(this, "Video guardado en: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error al guardar el archivo de video", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendVideo64ToServer(videoBytes: ByteArray, producto: String, cliente: String) {
        val video64 = Base64.encodeToString(videoBytes, Base64.DEFAULT)

        val videoRequest = VideoRequest(
            idCliente = cliente,  // Asumiendo que 'cliente' es un ID de tipo String
            idProducto = producto.toInt(),  // Convertir 'producto' a un número entero
            nombre = nombreVideo,
            video = video64
        )
        //Log.d("VideoRequest", "Request Body: $videoRequest")
        loadingContainer.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicio-video-596275467600.us-central1.run.app/api/Video/")  // Aqui URL de microservicio
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.uploadVideo(videoRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                loadingContainer.visibility = View.GONE
                if (response.isSuccessful) {
                    Toast.makeText(this@CargarVideoActivity, "Video subido exitosamente", Toast.LENGTH_SHORT).show()

                } else {
                    // Imprimir el cuerpo de la respuesta en caso de error
                    try {
                        val errorBody = response.errorBody()?.string()
                        Log.e("UploadError", "Error en la subida del video: $errorBody")
                        Toast.makeText(this@CargarVideoActivity, "Error al subir el video", Toast.LENGTH_SHORT).show()
                    } catch (e: IOException) {
                        Log.e("UploadError", "Error al leer la respuesta del error: ${e.message}")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                loadingContainer.visibility = View.GONE
                Toast.makeText(this@CargarVideoActivity, "Error en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cargarClientesDesdeApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://tu-microservicio.com/api/") // Reemplaza por la base real
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        apiService.getClientes().enqueue(object : Callback<List<Cliente>> {
            override fun onResponse(call: Call<List<Cliente>>, response: Response<List<Cliente>>) {
                if (response.isSuccessful) {
                    val clientes = response.body() ?: emptyList()

                    listaClientes = clientes

                    val nombresClientes = clientes.map { "${it.nombre} ${it.apellido}" }
                    val adapter = ArrayAdapter(
                        this@CargarVideoActivity,
                        android.R.layout.simple_spinner_item,
                        listOf("Selecciona uno") + nombresClientes
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerC.adapter = adapter
                } else {
                    Toast.makeText(this@CargarVideoActivity, "Error al cargar clientes", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Cliente>>, t: Throwable) {
                t.printStackTrace()
                Toast.makeText(this@CargarVideoActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

}


