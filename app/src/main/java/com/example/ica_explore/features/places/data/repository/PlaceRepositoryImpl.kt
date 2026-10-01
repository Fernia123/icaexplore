package com.example.ica_explore.features.places.data.repository

import com.example.ica_explore.features.places.data.datasource.remote.PlaceRemoteDataSource
import com.example.ica_explore.features.places.domain.model.Place
import com.example.ica_explore.features.places.data.mapper.toDomain
import com.example.ica_explore.features.places.domain.repository.PlaceRepository

/**
 * Adaptador (Adapter) en Arquitectura Hexagonal.
 * Implementa la interfaz (Puerto) definida en el Dominio.
 * Aquí inyectaremos el Data Source (Supabase) y opcionalmente la BD local (Room).
 */
class PlaceRepositoryImpl(
    private val remoteDataSource: PlaceRemoteDataSource
    // private val localDataSource: PlaceLocalDataSource // Cuando agregues Room
) : PlaceRepository {

    override suspend fun getPlacesByCategory(categoryId: String): Result<List<Place>> {
        return try {
            val placesDto = remoteDataSource.getPlacesByCategory(categoryId)
            // Mapeamos de DTO (Datos) a Entidad pura (Dominio)
            Result.success(placesDto.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPlaceById(id: String): Result<Place> {
        return try {
            val placeDto = remoteDataSource.getPlaceById(id)
            if (placeDto != null) {
                Result.success(placeDto.toDomain())
            } else {
                Result.failure(Exception("Place not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPopularPlaces(limit: Int): Result<List<Place>> {
        return try {
            val placesDto = remoteDataSource.getPopularPlaces(limit)
            Result.success(placesDto.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
