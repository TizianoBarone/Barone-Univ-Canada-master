package com.tba.universidades

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.tba.universidades.AppDatabase
import com.tba.universidades.R
import com.tba.universidades.User
import com.tba.universidades.UserDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedPreferences : AppCompatActivity() {
    private lateinit var tvDato: TextView
    private lateinit var spinnerNacionalidad: Spinner
    private var currentUser: User? = null // Cambiado a nullable para manejar la inicialización diferida
    private lateinit var userDao: UserDao

    @SuppressLint("MissingInflatedId", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_preferences) // Establece la vista primero
        val switchTema = findViewById<Switch>(R.id.switchTema)
        switchTema.isChecked = esModoOscuroActivado()

        // Establecer el listener para el switch
        switchTema.setOnCheckedChangeListener { _, isChecked ->
            cambiarTema(isChecked)
        }

        // Inicializa la UI que no depende de currentUser
        tvDato = findViewById(R.id.tvDato)
        spinnerNacionalidad = findViewById(R.id.spinnerNacionalidad)
        inicializarSpinnerNacionalidad()

        // Inicializar el usuario de manera asíncrona
        inicializarUsuarioAsincronamente()
    }

    private fun inicializarUsuarioAsincronamente() {
        lifecycleScope.launch(Dispatchers.IO) {
            userDao = AppDatabase.getInstance(applicationContext).userDao()
            currentUser = userDao.getCurrentUser(applicationContext)

            // Verifica si currentUser está inicializado antes de proceder
            if (currentUser != null) {
                launch(Dispatchers.Main) {
                    aplicarTema()
                    // Continuar con cualquier otra inicialización que dependa de currentUser aquí
                    cargarPreferencias()
                }
            } else {
                // Manejar el caso de que currentUser sea null
                // Por ejemplo, redirigir al usuario a una pantalla de login
            }
        }
    }

    private fun aplicarTema() {
        currentUser?.let { user ->
            val pref = getSharedPreferences("prefs_${user.uid}", MODE_PRIVATE)
            if (pref.getBoolean("modoOscuro", false)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        } ?: run {
            // Aplicar un tema predeterminado si currentUser es null
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun cambiarTema(modoOscuro: Boolean) {
        val editor = getSharedPreferences("prefs_${currentUser?.uid}", MODE_PRIVATE).edit()
        editor.putBoolean("modoOscuro", modoOscuro)
        editor.apply()

        AppCompatDelegate.setDefaultNightMode(
                if (modoOscuro) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        // Reiniciar la actividad para aplicar el tema inmediatamente
        recreate()
    }


    private fun esModoOscuroActivado(): Boolean {
        val pref = getSharedPreferences("prefs_${currentUser?.uid}", MODE_PRIVATE)
        return pref.getBoolean("modoOscuro", false)
    }

    private fun inicializarSpinnerNacionalidad() {
        val nacionalidades = listOf("España", "Argentina", "Canadá", "Estados Unidos", "Francia", "Alemania", "Italia", "Reino Unido")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, nacionalidades)
        spinnerNacionalidad.adapter = adapter

        spinnerNacionalidad.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val nacionalidadSeleccionada = parent.getItemAtPosition(position).toString()
                guardarPreferenciaNacionalidad(nacionalidadSeleccionada)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Opcional: Manejar ningún elemento seleccionado
            }
        }
    }

    private fun cargarPreferencias() {
        val pref = getSharedPreferences("prefs_${currentUser?.uid}", MODE_PRIVATE)
        val nacionalidadFavorita = pref.getString("nacionalidadFavorita", "España") // Valor por defecto: España
        spinnerNacionalidad.setSelection((spinnerNacionalidad.adapter as ArrayAdapter<String>).getPosition(nacionalidadFavorita))
    }

    private fun guardarPreferenciaNacionalidad(nacionalidad: String) {
        val editor = getSharedPreferences("prefs_${currentUser?.uid}", MODE_PRIVATE).edit()
        editor.putString("nacionalidadFavorita", nacionalidad)
        editor.apply()
    }
}
