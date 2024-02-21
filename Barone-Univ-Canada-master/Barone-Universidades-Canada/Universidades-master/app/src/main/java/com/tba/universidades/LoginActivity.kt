package com.tba.universidades

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        buttonRegister = findViewById(R.id.buttonRegister)

        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                login(username, password)
            } else {
                showToast("Por favor, completa todos los campos")
            }
        }

        buttonRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login(username: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = AppDatabase.getInstance(applicationContext).userDao().getUserByUsernameAndPassword(username, password)
            if (user != null) {
                showToast("Inicio de sesión exitoso")
                startActivity(Intent(this@LoginActivity, MenuActivity::class.java))
                finish()
            } else {
                showToast("Credenciales incorrectas")
            }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}
