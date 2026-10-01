package com.example.ica_explore.features.auth.data.datasource.remote

import io.github.jan.supabase.auth.user.UserInfo

interface AuthRemoteDataSource {
    suspend fun signUp(email: String, password: String): UserInfo?
    suspend fun signIn(email: String, password: String)
    suspend fun signOut()
    suspend fun getCurrentUser(): UserInfo?
}
