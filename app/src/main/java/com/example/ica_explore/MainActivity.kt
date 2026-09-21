package com.example.ica_explore

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ica_explore.data.obtenerLugaresDeIca

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // ══════ PRUEBA TEMPORAL ══════
        val lugares = obtenerLugaresDeIca()
        Log.d("PRUEBA", "Total de lugares: ${lugares.size}")
        Log.d("PRUEBA", "Primero: ${lugares[0].nombre} en ${lugares[0].latitud}")
        // ═══════════════════════════════════════════════════════════════════
    }
}