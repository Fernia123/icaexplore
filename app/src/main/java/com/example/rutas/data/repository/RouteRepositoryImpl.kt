package com.example.rutas.data.repository

import com.example.rutas.data.remote.OrsApiService
import com.example.rutas.data.remote.RouteResponseDto

class RouteRepositoryImpl(
    private val apiService: OrsApiService,
    private val apiKey: String
) {
    suspend fun fetchRoute(
        profile: String,
        originLat: Double,
        originLng: Double,
        destLat: Double,
        destLng: Double
    ): Result<RouteResponseDto> {
        return try {
            val startFormatted = "$originLng,$originLat"
            val endFormatted = "$destLng,$destLat"

            val response = apiService.getRoute(
                profile = profile,
                apiKey = apiKey,
                start = startFormatted,
                end = endFormatted
            )

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}