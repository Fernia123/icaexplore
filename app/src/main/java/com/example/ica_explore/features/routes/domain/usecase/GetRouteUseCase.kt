package com.example.ica_explore.features.routes.domain.usecase

import com.example.ica_explore.features.routes.data.remote.model.RouteResponseDto
import com.example.ica_explore.features.routes.data.repository.RouteRepositoryImpl

class GetRouteUseCase(
    private val repository: RouteRepositoryImpl = RouteRepositoryImpl()
) {
    suspend operator fun invoke(
        profile: String = "driving-car",
        originLat: Double,
        originLng: Double,
        destLat: Double,
        destLng: Double
    ): Result<RouteResponseDto> {
        if (originLat == destLat && originLng == destLng) {
            return Result.failure(IllegalArgumentException("El origen y el destino no pueden ser idénticos."))
        }
        return repository.fetchRoute(profile, originLat, originLng, destLat, destLng)
    }
}
