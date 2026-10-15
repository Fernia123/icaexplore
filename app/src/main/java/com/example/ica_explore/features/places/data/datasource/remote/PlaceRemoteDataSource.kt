package com.example.ica_explore.features.places.data.datasource.remote

import com.example.ica_explore.features.places.data.datasource.remote.model.PlaceDto

/**
 * Interfaz para la fuente de datos remota (Supabase).
 * Pertenece a la capa de Datos.
 */
interface PlaceRemoteDataSource {
    suspend fun getPlacesByCategory(categoryId: String): List<PlaceDto>
    suspend fun getPlaceById(id: String): PlaceDto?
    suspend fun getPopularPlaces(limit: Int): List<PlaceDto>
}
