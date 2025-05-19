package com.uxdesign.ccp_frontend

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.uxdesign.cpp.R
import com.uxdesign.cpp.utils.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var usuarioText: EditText
    private lateinit var contraseniaText: EditText
    private var idUsuario = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val textVersion = findViewById<TextView>(R.id.textVersion)
        val versionName = packageManager.getPackageInfo(packageName, 0).versionName
        textVersion.text = "Versión: $versionName"

        usuarioText = findViewById(R.id.editUsuario)
        contraseniaText = findViewById(R.id.editClave)

        val buttonIngresar: Button = findViewById(R.id.buttonIngresar)
        buttonIngresar.setOnClickListener {
            if (!validarCampos()) {
                return@setOnClickListener
            }
            //validarUsuario(usuarioText.text.toString(), contraseniaText.text.toString())
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun validarCampos(): Boolean {
        val usuario = usuarioText.text.toString()
        if (usuario.isEmpty()) {
            usuarioText.error = "El usuario no puede estar vacío"
            return false
        }

        val contrasenia = contraseniaText.text.toString()
        if (contrasenia.isEmpty()) {
            contraseniaText.error = "La contraseña no puede estar vacía"
            return false
        }
        return true
    }

    private fun validarUsuario(usuario: String, contrasenia: String) {
        val login = LoginRequest(
            username = usuario,
            contrasena = contrasenia,
            aplicacion = 2
        )

        val retrofit = Retrofit.Builder()
            .baseUrl("https://usuarios-596275467600.us-central1.run.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        Log.d("LOGIN_REQUEST", Gson().toJson(login))
        val apiService = retrofit.create(ApiService::class.java)
        apiService.login(login).enqueue(object : Callback<RespuestaLogin> {
            override fun onResponse(
                call: Call<RespuestaLogin>,
                response: Response<RespuestaLogin>
            ) {
                val loginResponse = response.body()
                Log.d("LOGIN_RESPONSE", Gson().toJson(loginResponse))
                if (response.isSuccessful && loginResponse != null) {
                    if (loginResponse.menu.equals("azul", ignoreCase = true)) {
                        val idtemp: String = loginResponse.idusuario
                        val token = loginResponse.token
                        TokenManager.saveToken(this@MainActivity, loginResponse.token)
                        traerIdCliente(idtemp)
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            loginResponse.mensaje,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        loginResponse?.mensaje ?: "No fue posible autenticas al usuario",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<RespuestaLogin>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Usuario no encontrado",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }

    private fun traerIdCliente(usuario: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://usuarios-596275467600.us-central1.run.app/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(ApiService::class.java)
        apiService.getIdCliente(usuario).enqueue(object : Callback<RespuestaUsuario> {
            override fun onResponse(
                call: Call<RespuestaUsuario>,
                response: Response<RespuestaUsuario>
            ) {
                val loginResponse = response.body()
                if (response.isSuccessful && loginResponse != null) {
                    Log.i("ID_USUARIO", Gson().toJson(loginResponse))
                    idUsuario = loginResponse.idPerfil
                    val intent = Intent(this@MainActivity, MenuActivity::class.java)
                    intent.putExtra("id_usuario", idUsuario)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "No fue posible encontrar id de cliente",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<RespuestaUsuario>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Usuario no encontrado",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
    }
}