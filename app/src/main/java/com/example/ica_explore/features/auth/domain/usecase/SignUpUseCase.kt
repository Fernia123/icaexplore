package com.example.ica_explore.features.auth.domain.usecase

import com.example.ica_explore.features.auth.domain.model.User
import com.example.ica_explore.features.auth.domain.repository.AuthRepository

class SignUpUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank() || password.length < 6) {
            return Result.failure(IllegalArgumentException("Email inválido o contraseña muy corta (mínimo 6 caracteres)"))
        }
        return repository.signUp(email, password)
    }
}
