package com.example.ica_explore.features.categories.data.repository

import com.example.ica_explore.features.categories.data.datasource.remote.CategoryRemoteDataSource
import com.example.ica_explore.features.categories.domain.model.Category
import com.example.ica_explore.features.categories.data.mapper.toDomain
import com.example.ica_explore.features.categories.domain.repository.CategoryRepository

class CategoryRepositoryImpl(
    private val remoteDataSource: CategoryRemoteDataSource
) : CategoryRepository {

    override suspend fun getCategories(): Result<List<Category>> {
        return try {
            val dtos = remoteDataSource.getCategories()
            Result.success(dtos.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
