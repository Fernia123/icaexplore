package com.example.ica_explore.features.categories.domain.usecase

import com.example.ica_explore.features.categories.domain.model.Category
import com.example.ica_explore.features.categories.domain.repository.CategoryRepository

class GetCategoriesUseCase(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(): Result<List<Category>> {
        // Obtenemos las categorías; podrías agregar validaciones extra si fuera necesario
        return repository.getCategories()
    }
}
