package com.example.ica_explore.util


import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.ica_explore.core.map.IcaMap
import com.example.ica_explore.features.places.presentation.HomeActivity
import com.example.ica_explore.viewmodel.MapVM
import kotlinx.coroutines.launch
import org.maplibre.android.maps.Style

/**
 * conecta el cerebro (MapVM) con la cara de la pantalla (HomeActivity).
 *
 * Es una "función de extensión" de HomeActivity: le agrega una habilidad
 * nueva a la pantalla SIN modificar su código interno — como un adaptador
 * de enchufe que se conecta por fuera del aparato.
 *
 * Solo se llama UNA vez, después de que el estilo del mapa termina de cargar.
 */
fun HomeActivity.conectarMapVM(style: Style, mapaTieneDatosPropios: () -> Boolean) {
    //                                                ↑ una "pregunta" que se responde afuera

    val mapVM = ViewModelProvider(this)[MapVM::class.java]

    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            mapVM.estado.collect { uiState ->
                // Pinta la semilla SOLO si el mapa está huérfano de datos
                if (uiState.geoJsonLugares.isNotEmpty() && !mapaTieneDatosPropios()) {
                    IcaMap.updateData(style, uiState.geoJsonLugares)
                    Log.d("PuenteMapVM", "📌 Pines del MapVM pintados en el mapa")
                }
            }
        }
    }
}
