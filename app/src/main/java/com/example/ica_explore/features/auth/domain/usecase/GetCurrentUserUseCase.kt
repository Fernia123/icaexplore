package com.example.ica_explore.features.auth.domain.usecase

import com.example.ica_explore.features.auth.domain.model.User
import com.example.ica_explore.features.auth.domain.repository.AuthRepository

class GetCurrentUserUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Result<User?> {
        return repository.getCurrentUser()
    }
}
