package com.example.ica_explore.features.categories.data.datasource.remote

import com.example.ica_explore.features.categories.data.datasource.remote.model.CategoryDto

interface CategoryRemoteDataSource {
    suspend fun getCategories(): List<CategoryDto>
}
