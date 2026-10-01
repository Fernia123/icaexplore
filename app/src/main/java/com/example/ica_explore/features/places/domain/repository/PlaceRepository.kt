package com.example.ica_explore.features.places.domain.repository

import com.example.ica_explore.features.places.domain.model.Place

/**
 * Puerto (Port) en Arquitectura Hexagonal.
 * Define los contratos (interfaces) que la capa de Datos debe implementar.
 * El Dominio dicta QUÉ se necesita, la capa de Datos decide CÓMO obtenerlo.
 */
interface PlaceRepository {
    suspend fun getPlacesByCategory(categoryId: String): Result<List<Place>>
    suspend fun getPlaceById(id: String): Result<Place>
    suspend fun getPopularPlaces(limit: Int): Result<List<Place>>
    // Aquí puedes agregar más funciones según el Anexo C del documento
}
