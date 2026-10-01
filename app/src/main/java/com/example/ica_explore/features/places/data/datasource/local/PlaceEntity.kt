package com.example.ica_explore.features.places.data.datasource.local

// import androidx.room.Entity
// import androidx.room.PrimaryKey

// @Entity(tableName = "places")
data class PlaceEntity(
    // @PrimaryKey
    val id: String,
    val name: String,
    val categoryId: String,
    val description: String?,
    val address: String?,
    val latitude: Double,
    val longitude: Double,
    val phone: String?,
    val photoUrl: String?,
    val ratingAvg: Double,
    val reviewsCount: Int,
    val popularityIdx: Double
)
