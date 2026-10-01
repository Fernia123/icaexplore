package com.example.ica_explore.features.auth.domain.usecase

import com.example.ica_explore.features.auth.domain.repository.AuthRepository

class SignOutUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.signOut()
    }
}
