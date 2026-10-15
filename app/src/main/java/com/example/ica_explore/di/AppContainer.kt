package com.example.ica_explore.di

import com.example.ica_explore.features.auth.data.datasource.remote.AuthRemoteDataSourceImpl
import com.example.ica_explore.features.auth.data.repository.AuthRepositoryImpl
import com.example.ica_explore.features.auth.domain.repository.AuthRepository
import com.example.ica_explore.features.auth.domain.usecase.GetCurrentUserUseCase
import com.example.ica_explore.features.auth.domain.usecase.SignInUseCase
import com.example.ica_explore.features.auth.domain.usecase.SignOutUseCase
import com.example.ica_explore.features.auth.domain.usecase.SignUpUseCase
import com.example.ica_explore.features.categories.data.datasource.remote.CategoryRemoteDataSourceImpl
import com.example.ica_explore.features.categories.data.repository.CategoryRepositoryImpl
import com.example.ica_explore.features.categories.domain.repository.CategoryRepository
import com.example.ica_explore.features.categories.domain.usecase.GetCategoriesUseCase
import com.example.ica_explore.features.places.data.datasource.remote.PlaceRemoteDataSourceImpl
import com.example.ica_explore.features.places.data.repository.PlaceRepositoryImpl
import com.example.ica_explore.features.places.domain.repository.PlaceRepository
import com.example.ica_explore.features.places.domain.usecase.GetPopularPlacesUseCase

/**
 * Contenedor de dependencias simple (Service Locator).
 *
 * Ensambla manualmente las capas de Datos y Dominio siguiendo la
 * Arquitectura Limpia del proyecto. En el futuro puede reemplazarse
 * por Koin o Hilt sin cambiar las clases de features.
 */
object AppContainer {

    private val supabaseClient by lazy { SupabaseClientProvider.client }

    // --- Repositorios ---

    private val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(AuthRemoteDataSourceImpl(supabaseClient))
    }

    private val placeRepository: PlaceRepository by lazy {
        PlaceRepositoryImpl(PlaceRemoteDataSourceImpl(supabaseClient))
    }

    private val categoryRepository: CategoryRepository by lazy {
        CategoryRepositoryImpl(CategoryRemoteDataSourceImpl(supabaseClient))
    }

    // --- Casos de Uso (Auth) ---

    val signInUseCase: SignInUseCase by lazy { SignInUseCase(authRepository) }
    val signUpUseCase: SignUpUseCase by lazy { SignUpUseCase(authRepository) }
    val signOutUseCase: SignOutUseCase by lazy { SignOutUseCase(authRepository) }
    val getCurrentUserUseCase: GetCurrentUserUseCase by lazy { GetCurrentUserUseCase(authRepository) }

    // --- Casos de Uso (Places) ---

    val getPopularPlacesUseCase: GetPopularPlacesUseCase by lazy {
        GetPopularPlacesUseCase(placeRepository)
    }

    // --- Casos de Uso (Categories) ---

    val getCategoriesUseCase: GetCategoriesUseCase by lazy {
        GetCategoriesUseCase(categoryRepository)
    }
}
