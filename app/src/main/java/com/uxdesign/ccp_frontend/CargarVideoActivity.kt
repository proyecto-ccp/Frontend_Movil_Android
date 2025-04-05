package com.uxdesign.ccp_frontend

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uxdesign.cpp.R
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
    private val REQUEST_VIDEO_CAPTURE = 1
    private val REQUEST_VIDEO_GALLERY = 2
    private lateinit var videoBytes: ByteArray
    private lateinit var spinner: Spinner
    private lateinit var spinnerC: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cargar_video)

        spinner = findViewById(R.id.spinnerProductos)
        spinnerC = findViewById(R.id.spinnerClientes)

        val productos = listOf("Selecciona uno", "Producto 1", "Producto 2", "Producto 3")
        val clientes = listOf("Selecciona uno", "Cliente 1", "Cliente 2", "Cliente 3")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, productos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        val adapterc = ArrayAdapter(this, android.R.layout.simple_spinner_item, clientes)
        adapterc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerC.adapter = adapterc

        val buttonCargar = findViewById<Button>(R.id.buttonCargar)
        val buttonGaleria = findViewById<Button>(R.id.buttonGaleria)

        buttonCargar.setOnClickListener {
            val productoSeleccionado = spinner.selectedItem.toString()
            val clienteSeleccionado = spinnerC.selectedItem.toString()
            if (productoSeleccionado != "Selecciona uno" && clienteSeleccionado != "Selecciona uno")
            {
                val takeVideoIntent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
            } else {
                Toast.makeText(this, "Debes seleccionar un producto y un cliente", Toast.LENGTH_SHORT).show()
            }

        }

       
        buttonGaleria.setOnClickListener {
            val productoSeleccionado = spinner.selectedItem.toString()
            val clienteSeleccionado = spinnerC.selectedItem.toString()
            if (productoSeleccionado != "Selecciona uno" && clienteSeleccionado != "Selecciona uno")
            {
                val pickVideoIntent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                pickVideoIntent.type = "video/*"
                startActivityForResult(pickVideoIntent, REQUEST_VIDEO_GALLERY)
            } else {
                Toast.makeText(this, "Debes seleccionar un producto y un cliente", Toast.LENGTH_SHORT).show()
            }

        }

        val buttonVerLista: Button = findViewById<Button>(R.id.buttonLista)
        buttonVerLista.setOnClickListener {
            val intent = Intent(this, ListaVideosActivity::class.java)
            startActivity(intent)
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
                            val intent = Intent(this, LoadingActivity::class.java)
                            startActivity(intent)
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
                            saveVideoToFile(videoBytes)
                            //sendVideoToServer(videoBytes, spinner.selectedItem.toString())
                            //Toast.makeText(this, "Video capturado y convertido en bytes", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, LoadingActivity::class.java)
                            startActivity(intent)
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
            val file = File(filesDir, "video_guardado.mp4")
            val outputStream = FileOutputStream(file)

            outputStream.write(videoBytes)
            outputStream.close()

            Toast.makeText(this, "Video guardado en: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error al guardar el archivo de video", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendVideoToServer(videoBytes: ByteArray, producto: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://your.api.url/")  // Cambia la URL de tu microservicio
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)

        // Crear el RequestBody para el video
        val requestBody = RequestBody.create("video/mp4".toMediaTypeOrNull(), videoBytes)
        val videoPart = MultipartBody.Part.createFormData("video", "video.mp4", requestBody)

        // Crear el RequestBody para el producto
        val productoBody = RequestBody.create("text/plain".toMediaTypeOrNull(), producto)

        // Enviar la solicitud
        apiService.uploadVideo(videoPart, productoBody).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CargarVideoActivity, "Video subido exitosamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@CargarVideoActivity, LoadingActivity::class.java)
                } else {
                    Toast.makeText(this@CargarVideoActivity, "Error al subir el video", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@CargarVideoActivity, "Error en la conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

}