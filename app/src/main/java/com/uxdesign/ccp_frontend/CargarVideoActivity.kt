package com.uxdesign.ccp_frontend

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import android.util.Base64
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.uxdesign.cpp.R
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
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

        val productos = listOf("Selecciona uno", 1, 2, 3)
        val clientes = listOf("Selecciona uno", "550e8400-e29b-41d4-a716-446655440000", "Cliente2", "Cliente3")

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
                            val videoName: String = getFileNameFromUri(videoUri).toString()
                            val inputStream: InputStream = contentResolver.openInputStream(videoUri)!!
                            videoBytes = inputStream.readBytes()
                            saveVideoToFile(videoBytes)
                            sendVideo64ToServer(videoBytes, spinner.selectedItem.toString(), spinnerC.selectedItem.toString(), videoName)

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
                            val videoName : String = getFileNameFromUri(videoUri).toString()
                            val inputStream: InputStream = contentResolver.openInputStream(videoUri)!!
                            videoBytes = inputStream.readBytes()
                            saveVideoToFile(videoBytes)
                                // sendVideoToServer(videoBytes, spinner.selectedItem.toString(), spinnerC.selectedItem.toString(), videoName)
                            sendVideo64ToServer(videoBytes, spinner.selectedItem.toString(), spinnerC.selectedItem.toString(), videoName)

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

    private fun getFileNameFromUri(uri: Uri): String? {
        var fileName = "default.mp4"
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst()) {
                fileName = it.getString(nameIndex) ?: "default.mp4"
            }
        }
        return fileName
    }

    private fun saveVideoToFile(videoBytes: ByteArray) {
        try {
            val file = File(filesDir, "video_guardado.mp4")
            val outputStream = FileOutputStream(file)

            outputStream.write(videoBytes)
            outputStream.close()

            //Toast.makeText(this, "Video guardado en: ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Error al guardar el archivo de video", Toast.LENGTH_SHORT).show()
        }
    }

    //private fun sendVideoToServer(videoBytes: ByteArray, producto: String, cliente: String, videoName: String) {
      //  val retrofit = Retrofit.Builder()
        //    .baseUrl("https://servicio-video-596275467600.us-central1.run.app/api/Video/CargarVideo/")  // Aqui URL de microservicio
          //  .addConverterFactory(GsonConverterFactory.create())
            //.build()

        //val apiService = retrofit.create(ApiService::class.java)

        // Crear el RequestBody para el video
        //val requestBody = RequestBody.create("video/mp4".toMediaTypeOrNull(), videoBytes)
        //val videoPart = MultipartBody.Part.createFormData("video", videoName, requestBody)

        // Crear los datos adicionales en formato JSON
        //val videoRequest = VideoRequest(
          //  idCliente = cliente,  // Asumiendo que 'cliente' es un ID de tipo String
            //idProducto = producto.toInt(),  // Convertir 'producto' a un número entero
            //nombre = videoName,
            //video = "kjhh"
      //  )

        //val gson = Gson()
       // val jsonRequest = gson.toJson(videoRequest)
        //val requestBodyJson = RequestBody.create("application/json".toMediaTypeOrNull(), jsonRequest)

        //Log.d("HTTP", "JSON Request: $jsonRequest")

        // Enviar la solicitud
        //apiService.uploadVideo(videoPart, requestBodyJson).enqueue(object : Callback<ResponseBody> {
          //  override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            //    if (response.isSuccessful) {
           //         Toast.makeText(this@CargarVideoActivity, "Video subido exitosamente", Toast.LENGTH_SHORT).show()
            //        val intent = Intent(this@CargarVideoActivity, ListaVideosActivity::class.java)
            //        intent.putExtra("idProducto", producto.toInt())
            //        intent.putExtra("idCliente", cliente)
             //       intent.putExtra("videoName", videoName)
               //     startActivity(intent)
             //   } else {
             //       Toast.makeText(this@CargarVideoActivity, "Error al subir el video", Toast.LENGTH_SHORT).show()
            //    }
           // }

     //       override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
       //         Toast.makeText(this@CargarVideoActivity, "Error en la conexión", Toast.LENGTH_SHORT).show()
      //      }
      //  })
    //}

    private fun sendVideo64ToServer(videoBytes: ByteArray, producto: String, cliente: String, videoName: String) {
        val video64 = Base64.encodeToString(videoBytes, Base64.DEFAULT)

        val videoRequest = VideoRequest(
            idCliente = cliente,  // Asumiendo que 'cliente' es un ID de tipo String
            idProducto = producto.toInt(),  // Convertir 'producto' a un número entero
            nombre = videoName,
            video = video64
        )

        val retrofit = Retrofit.Builder()
            .baseUrl("https://servicio-video-596275467600.us-central1.run.app/api/Video/CargarVideo/")  // Aqui URL de microservicio
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        apiService.uploadVideo(videoRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CargarVideoActivity, "Video subido exitosamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@CargarVideoActivity, ListaVideosActivity::class.java)
                    intent.putExtra("idProducto", producto.toInt())
                    intent.putExtra("idCliente", cliente)
                    intent.putExtra("videoName", videoName)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@CargarVideoActivity, "Error al subir el video", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@CargarVideoActivity, "Error en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}


