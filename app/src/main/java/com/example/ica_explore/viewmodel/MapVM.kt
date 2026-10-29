package com.example.ica_explore.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ica_explore.data.obtenerLugaresDeIca
import com.example.ica_explore.util.GeoJsonConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * MapVM: el "cerebro" de la pantalla del mapa (convención 24.2: marca VM).
 *
 * ¿Por qué existe?
 *  1. Generar el GeoJSON en la pantalla congelaba la app ("Skipped 31 frames").
 *     → Aquí se genera en hilo de fondo (corrutinas: el mesero delega a la cocina 🍳).
 *  2. El estado guardado en la pantalla muere al rotar el celular.
 *     → El ViewModel sobrevive a la rotación.
 *
 * Este cerebro NO sabe si la "cara" es Compose o Views/XML. Por eso el líder
 * puede integrarlo al prototipo tal cual, sin cambiar nada.
 */
class MapVM : ViewModel() {

    // DATOS CRUDOS. Si el líder conecta Supabase, SOLO esta línea cambia por su
    // repositorio; el resto del cerebro no se toca (ese es el valor del VM).
    private val lugares = obtenerLugaresDeIca()

    // CAMPANITA INTERNA 🔔 (mutable): solo este VM la toca. El guion bajo (_)
    // es convención de "uso interno".
    private val _estado = MutableStateFlow(MapUiState())

    // CAMPANITA PÚBLICA 🔕 (solo lectura): la UI solo escucha, nunca toca.
    val estado: StateFlow<MapUiState> = _estado.asStateFlow()

    /** Corre UNA vez al crear el MapVM (el "onCreate" de esta clase). */
    init {
        cargarLugares()
    }

    /** Convierte los lugares a GeoJSON en hilo de fondo y avisa por la campanita. */
    private fun cargarLugares() {
        // viewModelScope: zona de corrutinas que muere con el VM (sin fugas de memoria).
        viewModelScope.launch(Dispatchers.Default) {
            Log.d("MapVM", "Cocinando GeoJSON de ${lugares.size} lugares en hilo de fondo...")
            val geoJson = GeoJsonConverter.convertirAGeoJson(lugares)
            _estado.update { estadoActual ->
                estadoActual.copy(
                    cargando = false,
                    lugares = lugares,
                    geoJsonLugares = geoJson
                )
            }
            Log.d("MapVM", "Campanita tocada 🔔 GeoJSON listo: ${geoJson.take(80)}...")
        }
    }

    /**
     * LISTO PARA LOS CHIPS: filtra por categoría ("turistico", "restaurante").
     * Si llega null o "todos", muestra todo.
     */
    fun filtrarPorCategoria(categoria: String?) {
        val listaFiltrada = if (categoria == null || categoria == "todos") {
            lugares
        } else {
            lugares.filter { it.categoria == categoria } // se queda con los que coinciden
        }
        viewModelScope.launch(Dispatchers.Default) {
            val geoJson = GeoJsonConverter.convertirAGeoJson(listaFiltrada)
            _estado.update { it.copy(lugares = listaFiltrada, geoJsonLugares = geoJson) }
        }
    }

    /**
     * LISTO PARA EL BOTTOMSHEET: marca el lugar tocado como seleccionado.
     * Un id inexistente deja la caja igual (no crashea).
     */
    fun seleccionarLugar(id: Int) {
        _estado.update { estadoActual ->
            estadoActual.copy(
                lugarSeleccionado = lugares.firstOrNull { it.id == id } // el primero que coincida, o null
            )
        }
    }

    /** Cierra el BottomSheet: pone el interruptor de nuevo en null. */
    fun cerrarSeleccion() {
        _estado.update { it.copy(lugarSeleccionado = null) }
    }
}