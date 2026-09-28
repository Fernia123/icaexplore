package com.example.rutas

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.rutas.data.remote.OrsApiService
import com.example.rutas.data.repository.RouteRepositoryImpl
import com.example.rutas.domain.GetRouteUseCase
import com.example.rutas.utils.NavigationFallback
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {

    private var statusText by mutableStateOf("Calculando ruta...")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Instancia única de Retrofit apuntando a api.heigit.org
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.heigit.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(OrsApiService::class.java)
        val repository = RouteRepositoryImpl(apiService, BuildConfig.ORS_API_KEY)
        val getRouteUseCase = GetRouteUseCase(repository)

        lifecycleScope.launch {
            val result = getRouteUseCase(
                profile = "driving-car",
                originLat = -14.0678,
                originLng = -75.7286,
                destLat = -14.0875,
                destLng = -75.7633
            )

            result.onSuccess { response ->
                val distanceKm = response.routes.firstOrNull()?.summary?.distance?.div(1000) ?: 0.0
                Log.d("ICA_EXPLORER", "¡Ruta calculada con éxito! Distancia: $distanceKm km")

                statusText = "Ruta calculada con éxito:\nPlaza de Armas -> Huacachina\nDistancia: %.2f km".format(distanceKm)
            }.onFailure { error ->
                Log.e("ICA_EXPLORER", "Error en API ORS: ${error.message}. Activando Fallback...")

                statusText = "Error al consultar API: ${error.message}\nAbriendo mapa externo..."
                NavigationFallback.openExternalMap(this@MainActivity, -14.0875, -75.7633)
            }
        }

        setContent {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = statusText)
            }
        }
    }
}