package com.example.ica_explore.features.categories.domain.repository

import com.example.ica_explore.features.categories.domain.model.Category

interface CategoryRepository {
    suspend fun getCategories(): Result<List<Category>>
}
