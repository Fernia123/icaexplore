package com.example.ica_explore.features.routes.data.remote.model

import com.google.gson.annotations.SerializedName

data class RouteResponseDto(
    @SerializedName("routes")
    val routes: List<RouteDto> = emptyList()
)

data class RouteDto(
    @SerializedName("summary")
    val summary: RouteSummaryDto,
    @SerializedName("geometry")
    val geometry: String? = null
)

data class RouteSummaryDto(
    @SerializedName("distance")
    val distance: Double,
    @SerializedName("duration")
    val duration: Double
)
