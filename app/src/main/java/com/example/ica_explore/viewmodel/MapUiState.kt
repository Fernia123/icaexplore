package com.example.ica_explore.viewmodel


import com.example.ica_explore.model.PlaceUiModel

/**
 * MapUiState: la "caja" que guarda TODO lo que la pantalla del mapa necesita
 * mostrar en un instante dado.
 *
 * La pantalla nunca calcula nada: solo mira esta caja y pinta lo que hay.
 * Cuando el MapVM cambia la caja, la UI se entera por la campanita (StateFlow).
 *
 * NOTA: un ViewModel con StateFlow NO depende de Compose — es lógica pura.
 * Funciona igual en el prototipo del líder (Views/XML) sin cambiar una línea.
 *
 * Convención 24.1: data class en PascalCase, propiedades en camelCase.
 *
 * @property cargando          true = "trabajando, espera". false = ya hay datos.
 * @property lugares           Lista de lugares lista para la UI.
 * @property geoJsonLugares    Texto GeoJSON que el mapa consume (generado en hilo de fondo).
 * @property lugarSeleccionado Lugar tocado. null = BottomSheet oculto; con datos = visible.
 */
data class MapUiState(
    val cargando: Boolean = true,
    val lugares: List<PlaceUiModel> = emptyList(),
    val geoJsonLugares: String = "",
    val lugarSeleccionado: PlaceUiModel? = null
)