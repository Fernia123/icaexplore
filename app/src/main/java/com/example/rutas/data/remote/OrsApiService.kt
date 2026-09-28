package com.example.rutas.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface OrsApiService {

    @GET("v2/directions/{profile}")
    suspend fun getRoute(
        @Path("profile") profile: String,
        @Header("Authorization") apiKey: String,
        @Query("start") start: String,
        @Query("end") end: String
    ): Response<RouteResponseDto>
}