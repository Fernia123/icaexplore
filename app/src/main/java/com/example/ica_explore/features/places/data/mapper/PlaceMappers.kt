package com.example.ica_explore.features.places.data.mapper

import com.example.ica_explore.features.places.data.datasource.local.PlaceEntity
import com.example.ica_explore.features.places.data.datasource.remote.model.PlaceDto
import com.example.ica_explore.features.places.domain.model.Place

fun PlaceDto.toDomain(): Place {
    return Place(
        id = id,
        name = name,
        categoryId = categoryId,
        description = description,
        address = address,
        latitude = latitude,
        longitude = longitude,
        phone = phone,
        photoUrl = photoUrl,
        ratingAvg = ratingAvg,
        reviewsCount = reviewsCount,
        popularityIdx = popularityIdx
    )
}

fun PlaceDto.toEntity(): PlaceEntity {
    return PlaceEntity(
        id = id,
        name = name,
        categoryId = categoryId,
        description = description,
        address = address,
        latitude = latitude,
        longitude = longitude,
        phone = phone,
        photoUrl = photoUrl,
        ratingAvg = ratingAvg,
        reviewsCount = reviewsCount,
        popularityIdx = popularityIdx
    )
}

fun PlaceEntity.toDomain(): Place {
    return Place(
        id = id,
        name = name,
        categoryId = categoryId,
        description = description,
        address = address,
        latitude = latitude,
        longitude = longitude,
        phone = phone,
        photoUrl = photoUrl,
        ratingAvg = ratingAvg,
        reviewsCount = reviewsCount,
        popularityIdx = popularityIdx
    )
}
