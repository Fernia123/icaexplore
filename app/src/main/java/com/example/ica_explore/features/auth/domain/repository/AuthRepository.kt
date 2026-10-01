package com.example.ica_explore.features.auth.domain.repository

import com.example.ica_explore.features.auth.domain.model.User

/**
 * Puerto de Autenticación.
 * Define las acciones que el dominio necesita para manejar usuarios.
 */
interface AuthRepository {
    suspend fun signUp(email: String, password: String): Result<User>
    suspend fun signIn(email: String, password: String): Result<User>
    suspend fun signOut(): Result<Unit>
    suspend fun getCurrentUser(): Result<User?>
}
