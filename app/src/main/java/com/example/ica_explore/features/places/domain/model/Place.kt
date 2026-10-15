package com.example.ica_explore.features.places.domain.model

/**
 * Entidad de Dominio: Place
 * Esta clase representa la información pura de negocio.
 * NO debe tener anotaciones de Supabase (@Serializable) ni de Room (@Entity).
 */
data class Place(
    val id: String,
    val name: String,
    val categoryId: String,
    val description: String?,
    val address: String?,
    val latitude: Double,
    val longitude: Double,
    val phone: String?,
    val photoUrl: String?,
    val ratingAvg: Double,
    val reviewsCount: Int,
    val popularityIdx: Double
)
