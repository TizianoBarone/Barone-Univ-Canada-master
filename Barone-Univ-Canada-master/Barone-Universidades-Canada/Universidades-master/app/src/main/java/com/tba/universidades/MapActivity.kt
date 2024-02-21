package com.tba.universidades

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var stateCoordinates: Map<String, LatLng>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Configurar el fragmento del mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map_container) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Inicializar el mapa de estados y coordenadas
        initStateCoordinates()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        try {
            mMap = googleMap
            // Obtener el estado de la universidad del Intent
            val stateProvince = intent.getStringExtra("stateProvince")
            if (stateProvince != null) {
                // Ejemplo de cómo mostrar la ubicación del estado al iniciar el mapa
                showLocation(stateProvince)
            } else {
                // Manejar el caso en que no se proporciona el estado
                Log.e("MapActivity", "No se proporcionó un estado válido.")
            }
        } catch (e: Exception) {
            Log.e("MapActivity", "Error en onMapReady: ${e.message}")
        }
    }


    private fun initStateCoordinates() {
        // Inicializar el mapa de estados y coordenadas
        stateCoordinates = mapOf(
            "Ontario" to LatLng(51.2538, -85.3232),
            "Quebec" to LatLng(52.9399, -73.5491),
            "Nova Scotia" to LatLng(44.6819, -63.7443),
            "New Brunswick" to LatLng(46.5653, -66.4619),
            "Manitoba" to LatLng(53.7609, -98.8139),
            "British Columbia" to LatLng(53.7267, -127.6476),
            "Prince Edward Island" to LatLng(46.5107, -63.4168),
            "Saskatchewan" to LatLng(52.9399, -106.4509),
            "Alberta" to LatLng(53.9333, -116.5765),
            "Newfoundland and Labrador" to LatLng(53.1355, -57.6604),
            "Northwest Territories" to LatLng(64.8255, -124.8457),
            "Yukon" to LatLng(64.2823, -135.0000),
            "Nunavut" to LatLng(70.2998, -83.1076)
        )
    }

    private fun showLocation(state: String) {
        // Verificar si el estado está en el mapa de coordenadas
        val coordinates = stateCoordinates[state]
        if (coordinates != null) {
            // Si se encuentra el estado, mostrarlo en el mapa
            mMap.addMarker(MarkerOptions().position(coordinates).title("Ubicación de $state"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 10f))
        } else {
            // Si el estado no está en el mapa de coordenadas, mostrar un mensaje de error
            Log.e("showLocation", "No se encontraron coordenadas para el estado: $state")
        }
    }
}
