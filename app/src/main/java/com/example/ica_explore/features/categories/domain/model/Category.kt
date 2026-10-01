package com.example.ica_explore.features.categories.domain.model

data class Category(
    val id: String,
    val name: String,
    val slug: String,
    val iconName: String?,
    val colorHex: String?,
    val sortOrder: Int
)
