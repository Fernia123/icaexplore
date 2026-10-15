package com.example.ica_explore.features.places.data.datasource.remote.model

import com.example.ica_explore.features.places.domain.model.Place
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 * DTO (Data Transfer Object) para la capa de Datos.
 * Aquí SÍ van las anotaciones de la base de datos o de la red (ej. @Serializable de Supabase).
 */
@Serializable
data class PlaceDto(
    val id: String, 
    val name: String, 
    @SerialName("category_id") val categoryId: String,
    val description: String?, 
    val address: String?,
    val latitude: Double, 
    val longitude: Double,
    val phone: String?, 
    @SerialName("photo_url") val photoUrl: String?,
    @SerialName("rating_avg") val ratingAvg: Double, 
    @SerialName("reviews_count") val reviewsCount: Int, 
    @SerialName("popularity_idx") val popularityIdx: Double
) {
    /**
     * Función de mapeo para convertir el DTO (Datos) a un objeto puro (Dominio).
     */
    
}


