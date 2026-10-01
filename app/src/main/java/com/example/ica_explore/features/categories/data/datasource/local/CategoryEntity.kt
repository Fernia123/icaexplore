package com.example.ica_explore.features.categories.data.datasource.local

// import androidx.room.Entity
// import androidx.room.PrimaryKey

// @Entity(tableName = "categories")
data class CategoryEntity(
    // @PrimaryKey
    val id: String,
    val name: String,
    val slug: String,
    val iconName: String?,
    val colorHex: String?,
    val sortOrder: Int
)
