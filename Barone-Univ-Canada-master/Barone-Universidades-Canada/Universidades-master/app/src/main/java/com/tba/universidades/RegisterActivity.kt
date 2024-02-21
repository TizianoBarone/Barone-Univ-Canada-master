// RegisterActivity.kt
package com.tba.universidades

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private lateinit var editTextUsername: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var editTextConfirmPassword: EditText
    private lateinit var buttonDoRegister: Button
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        editTextUsername = findViewById(R.id.registerUsername)
        editTextPassword = findViewById(R.id.registerPassword)
        editTextConfirmPassword = findViewById(R.id.registerConfirmPassword)
        buttonDoRegister = findViewById(R.id.buttonDoRegister)

        // Inicializar la base de datos correctamente
        db = AppDatabase.getInstance(applicationContext)

        buttonDoRegister.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()
            val confirmPassword = editTextConfirmPassword.text.toString()

            // Verificar que los campos no estén vacíos y que las contraseñas coincidan
            if (username.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    register(username, password)
                } else {
                    showToast("Las contraseñas no coinciden")
                }
            } else {
                showToast("Por favor, completa todos los campos")
            }
        }
    }

    private fun register(username: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Consultar si el usuario ya existe
                val existingUser = db.userDao().getUserByUsernameAndPassword(username, password)

                if (existingUser == null) {
                    // El usuario no existe, procede con el registro
                    val newUser = User(username = username, password = password)
                    db.userDao().insert(newUser)
                    // Notificar éxito en el hilo principal
                    showToast("Registro exitoso")
                    finish() // Cerrar esta actividad y volver a la de login
                } else {
                    // El usuario ya existe, mostrar mensaje de error
                    showToast("El usuario ya existe")
                }
            } catch (e: Exception) {
                // Manejar error de registro
                showToast("Error en el registro: ${e.message}")
            }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_SHORT).show()
        }
    }
}