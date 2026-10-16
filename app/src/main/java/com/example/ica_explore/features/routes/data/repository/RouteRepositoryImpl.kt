package com.example.ica_explore.features.routes.data.repository

import com.example.ica_explore.BuildConfig
import com.example.ica_explore.features.routes.data.remote.OrsApiService
import com.example.ica_explore.features.routes.data.remote.model.RouteResponseDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RouteRepositoryImpl(
    private val apiService: OrsApiService = createDefaultApiService(),
    private val apiKey: String = BuildConfig.ORS_API_KEY
) {
    suspend fun fetchRoute(
        profile: String,
        originLat: Double,
        originLng: Double,
        destLat: Double,
        destLng: Double
    ): Result<RouteResponseDto> {
        return try {
            val startFormatted = "$originLng,$originLat"
            val endFormatted = "$destLng,$destLat"

            val response = apiService.getRoute(
                profile = profile,
                apiKey = apiKey,
                start = startFormatted,
                end = endFormatted
            )

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        fun createDefaultApiService(): OrsApiService {
            return Retrofit.Builder()
                .baseUrl("https://api.heigit.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OrsApiService::class.java)
        }
    }
}
