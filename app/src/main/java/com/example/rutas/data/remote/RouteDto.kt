package com.example.rutas.data.remote

import com.google.gson.annotations.SerializedName

/**
 * DTOs para deserializar la respuesta JSON de la API de openrouteservice
 */
data class RouteResponseDto(
    @SerializedName("routes")
    val routes: List<RouteDto>
)

data class RouteDto(
    @SerializedName("summary")
    val summary: RouteSummaryDto,
    @SerializedName("geometry")
    val geometry: String // Geometría codificada en Encoded Polyline
)

data class RouteSummaryDto(
    @SerializedName("distance")
    val distance: Double, // Distancia en metros
    @SerializedName("duration")
    val duration: Double  // Duración estimada en segundos
)