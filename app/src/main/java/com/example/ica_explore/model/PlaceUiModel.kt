package com.example.ica_explore.model

/**
 * Modelo que representa un lugar de Ica (restaurante o centro turístico)
 * listo para mostrarse en el mapa.
 */
data class PlaceUiModel(
    val id: Int,
    val nombre: String,
    val categoria: String,     // Distintas categorias hoteles, restaurantes, lugares turisticos, etc.
    val latitud: Double,
    val longitud: Double,
    val descripcion: String = ""
)
