package com.example.ica_explore.features.auth.data.repository

import com.example.ica_explore.features.auth.data.datasource.remote.AuthRemoteDataSource
import com.example.ica_explore.features.auth.domain.model.User
import com.example.ica_explore.features.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun signUp(email: String, password: String): Result<User> {
        return try {
            val userInfo = remoteDataSource.signUp(email, password)
            if (userInfo != null) {
                Result.success(User(id = userInfo.id, email = userInfo.email))
            } else {
                Result.failure(Exception("No se pudo registrar el usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signIn(email: String, password: String): Result<User> {
        return try {
            remoteDataSource.signIn(email, password)
            val userInfo = remoteDataSource.getCurrentUser()
            if (userInfo != null) {
                Result.success(User(id = userInfo.id, email = userInfo.email))
            } else {
                Result.failure(Exception("No se pudo obtener el usuario tras el login"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signOut(): Result<Unit> {
        return try {
            remoteDataSource.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): Result<User?> {
        return try {
            val userInfo = remoteDataSource.getCurrentUser()
            if (userInfo != null) {
                Result.success(User(id = userInfo.id, email = userInfo.email))
            } else {
                Result.success(null) // No hay usuario logueado, es válido
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
