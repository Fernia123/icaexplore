package com.example.ica_explore.features.auth.domain.model

/**
 * Entidad de Usuario en el Dominio.
 * Contiene solo la información pura del usuario autenticado.
 */
data class User(
    val id: String,
    val email: String?
)
