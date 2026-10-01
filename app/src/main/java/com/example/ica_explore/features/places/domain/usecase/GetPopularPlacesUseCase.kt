package com.example.ica_explore.features.places.domain.usecase

import com.example.ica_explore.features.places.domain.model.Place
import com.example.ica_explore.features.places.domain.repository.PlaceRepository

/**
 * Caso de Uso (Interactor) en Arquitectura Limpia/Hexagonal.
 * Orquesta la lógica de negocio usando los puertos (repositorios).
 */
class GetPopularPlacesUseCase(
    private val repository: PlaceRepository
) {
    suspend operator fun invoke(limit: Int = 10): Result<List<Place>> {
        // Aquí puedes agregar validaciones de negocio antes de llamar al repositorio
        if (limit <= 0) return Result.failure(IllegalArgumentException("El límite debe ser mayor a 0"))
        
        return repository.getPopularPlaces(limit)
    }
}
