package com.example.ica_explore.features.categories.data.mapper

import com.example.ica_explore.features.categories.data.datasource.local.CategoryEntity
import com.example.ica_explore.features.categories.data.datasource.remote.model.CategoryDto
import com.example.ica_explore.features.categories.domain.model.Category

fun CategoryDto.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        slug = slug,
        iconName = iconName,
        colorHex = colorHex,
        sortOrder = sortOrder
    )
}

fun CategoryDto.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        slug = slug,
        iconName = iconName,
        colorHex = colorHex,
        sortOrder = sortOrder
    )
}

fun CategoryEntity.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        slug = slug,
        iconName = iconName,
        colorHex = colorHex,
        sortOrder = sortOrder
    )
}
