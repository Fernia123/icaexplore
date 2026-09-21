package com.example.rutas.domain

import com.example.rutas.data.remote.RouteResponseDto
import com.example.rutas.data.repository.RouteRepositoryImpl

class GetRouteUseCase(
    private val repository: RouteRepositoryImpl
) {
    /**
     * Ejecuta la solicitud de ruta aplicando validaciones previas
     */
    suspend operator fun invoke(
        profile: String,
        originLat: Double,
        originLng: Double,
        destLat: Double,
        destLng: Double
    ): Result<RouteResponseDto> {
        // Validación básica: evitar peticiones con coordenadas inválidas o idénticas
        if (originLat == destLat && originLng == destLng) {
            return Result.failure(IllegalArgumentException("El origen y el destino no pueden ser idénticos."))
        }

        return repository.fetchRoute(
            profile = profile,
            originLat = originLat,
            originLng = originLng,
            destLat = destLat,
            destLng = destLng
        )
    }
}