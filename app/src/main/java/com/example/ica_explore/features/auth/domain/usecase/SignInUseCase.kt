package com.example.ica_explore.features.auth.domain.usecase

import com.example.ica_explore.features.auth.domain.model.User
import com.example.ica_explore.features.auth.domain.repository.AuthRepository

class SignInUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Email y contraseña no pueden estar vacíos"))
        }
        return repository.signIn(email, password)
    }
}
