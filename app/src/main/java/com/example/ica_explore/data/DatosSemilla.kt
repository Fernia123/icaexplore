package com.example.ica_explore.data

import com.example.ica_explore.model.PlaceUiModel

/**
 * Datos de prueba (semilla) para el prototipo.
 * Más adelante el equipo de Backend reemplazará esto por datos reales de la API,
 * pero mientras tanto la UI y el mapa trabajan con esta lista.
 */
fun obtenerLugaresDeIca(): List<PlaceUiModel> {
    return listOf(
        PlaceUiModel(
            id = 1,
            nombre = "Oasis de Huacachina",
            categoria = "turistico",
            latitud = -14.0876,
            longitud = -75.7631,
            descripcion = "Oasis natural rodeado de dunas, a 5 km de Ica."
        ),
        PlaceUiModel(
            id = 2,
            nombre = "Plaza de Armas de Ica",
            categoria = "turistico",
            latitud = -14.0678,
            longitud = -75.7286,
            descripcion = "Plaza principal de la ciudad."
        ),
        PlaceUiModel(
            id = 3,
            nombre = "La Choza Náutica",
            categoria = "restaurante",
            latitud = -14.0762,
            longitud = -75.7321,
            descripcion = "Restaurante criollo famoso por su carapulcra."  // coords aprox, verificar
        )
        // TODO: agregar más lugares
    )
}
