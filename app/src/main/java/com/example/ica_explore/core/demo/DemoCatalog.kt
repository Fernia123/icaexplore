package com.example.ica_explore.core.demo

import com.example.ica_explore.features.categories.domain.model.Category
import com.example.ica_explore.features.places.domain.model.Place

/**
 * Datos de ejemplo (modo demo) de la ciudad de Ica.
 *
 * Permiten mostrar y navegar la interfaz sin necesidad de credenciales ni
 * conexión a Supabase. Solo se usan cuando el usuario entra con el botón
 * "Ver demo" de la pantalla de login.
 */
object DemoCatalog {

    /**
     * Categorías de ejemplo, equivalentes a la tabla `categories` de Supabase.
     */
    val categories: List<Category> = listOf(
        Category(
            id = "cat-gastronomia",
            name = "Gastronomía",
            slug = "gastronomia",
            iconName = "restaurant",
            colorHex = "#E53935",
            sortOrder = 1
        ),
        Category(
            id = "cat-iconicos",
            name = "Lugares icónicos",
            slug = "lugares-iconicos",
            iconName = "place",
            colorHex = "#1E88E5",
            sortOrder = 2
        ),
        Category(
            id = "cat-parques",
            name = "Parques y recreación",
            slug = "parques",
            iconName = "park",
            colorHex = "#43A047",
            sortOrder = 3
        ),
        Category(
            id = "cat-bodegas",
            name = "Bodegas y viñedos",
            slug = "bodegas",
            iconName = "wine_bar",
            colorHex = "#8E24AA",
            sortOrder = 4
        ),
        Category(
            id = "cat-aventura",
            name = "Aventura",
            slug = "aventura",
            iconName = "hiking",
            colorHex = "#FB8C00",
            sortOrder = 5
        )
    )

    /**
     * Lugares populares de ejemplo, ordenados por popularidad descendente
     * (equivale a `popularity_idx` en Supabase).
     */
    val popularPlaces: List<Place> = listOf(
        Place(
            id = "place-huacachina",
            name = "Oasis de Huacachina",
            categoryId = "cat-aventura",
            description = "Oasis natural rodeado de dunas de arena, ideal para el sandboarding y los paseos en tubulares.",
            address = "Balneario de Huacachina, Ica",
            latitude = -14.0875,
            longitude = -75.7628,
            phone = null,
            photoUrl = null,
            ratingAvg = 4.8,
            reviewsCount = 1240,
            popularityIdx = 0.98
        ),
        Place(
            id = "place-plaza-armas",
            name = "Plaza de Armas de Ica",
            categoryId = "cat-iconicos",
            description = "Plaza principal de la ciudad, rodeada de la Catedral y casonas coloniales.",
            address = "Calle Lima, Ica",
            latitude = -14.0678,
            longitude = -75.7286,
            phone = null,
            photoUrl = null,
            ratingAvg = 4.6,
            reviewsCount = 860,
            popularityIdx = 0.95
        ),
        Place(
            id = "place-tacama",
            name = "Bodega Tacama",
            categoryId = "cat-bodegas",
            description = "La bodega vitivinícola más antigua de Sudamérica, con tours y degustación.",
            address = "Prolongación Camino a La Tinguiña, Ica",
            latitude = -14.0325,
            longitude = -75.7150,
            phone = "+51 56 581030",
            photoUrl = null,
            ratingAvg = 4.7,
            reviewsCount = 980,
            popularityIdx = 0.92
        ),
        Place(
            id = "place-senor-luren",
            name = "Santuario del Señor de Luren",
            categoryId = "cat-iconicos",
            description = "Santuario del patrono de Ica, centro de una de las festividades religiosas más importantes del sur.",
            address = "Av. Señor de Luren, Ica",
            latitude = -14.0730,
            longitude = -75.7320,
            phone = null,
            photoUrl = null,
            ratingAvg = 4.7,
            reviewsCount = 540,
            popularityIdx = 0.90
        ),
        Place(
            id = "place-vista-alegre",
            name = "Bodega Vista Alegre",
            categoryId = "cat-bodegas",
            description = "Bodega tradicional con viñedos, producción de pisco y vinos, y restaurante campestre.",
            address = "Carretera a La Tinguiña, Ica",
            latitude = -14.0520,
            longitude = -75.7290,
            phone = "+51 56 219190",
            photoUrl = null,
            ratingAvg = 4.5,
            reviewsCount = 610,
            popularityIdx = 0.85
        ),
        Place(
            id = "place-olla-ica",
            name = "Restaurante La Olla de Ica",
            categoryId = "cat-gastronomia",
            description = "Cocina iqueña tradicional: tejas, chocotejas y platos de fondo con sazón local.",
            address = "Calle Bolívar, Ica",
            latitude = -14.0650,
            longitude = -75.7260,
            phone = null,
            photoUrl = null,
            ratingAvg = 4.6,
            reviewsCount = 430,
            popularityIdx = 0.88
        ),
        Place(
            id = "place-museo-regional",
            name = "Museo Regional de Ica",
            categoryId = "cat-iconicos",
            description = "Colección de cerámica, textiles y restos de las culturas Paracas, Nazca e Inca.",
            address = "Av. Ayabaca, Ica",
            latitude = -14.0726,
            longitude = -75.7296,
            phone = null,
            photoUrl = null,
            ratingAvg = 4.4,
            reviewsCount = 320,
            popularityIdx = 0.80
        ),
        Place(
            id = "place-parque-americas",
            name = "Parque de las Américas",
            categoryId = "cat-parques",
            description = "Amplio parque urbano con áreas verdes, ideal para caminar en familia.",
            address = "Av. Los Maestros, Ica",
            latitude = -14.0710,
            longitude = -75.7390,
            phone = null,
            photoUrl = null,
            ratingAvg = 4.3,
            reviewsCount = 210,
            popularityIdx = 0.75
        )
    )
}
