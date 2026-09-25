package com.example.ica_explore.util

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Utilidades geográficas para el motor de recomendaciones.
 */

/**
 * Calcula la distancia en KILÓMETROS entre dos puntos de la Tierra
 * usando la fórmula de Haversine (distancia en línea recta).
 *
 * @param lat1 latitud del punto A (ej: ubicación del usuario)
 * @param lng1 longitud del punto A
 * @param lat2 latitud del punto B (ej: el lugar turístico)
 * @param lng2 longitud del punto B
 * @return distancia en km (ej: 4.3 = 4 km y 300 metros)
 */

fun calcularDistanciaKm(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
    val radioTierraKm = 6371.0 //radio promedio de la tierra en km.

    //Convertir las DIFERENCIAS de coordenadas de grados a radianes
    //(las fórmulas trigonométricas  de kotlin trabajan en radianes, no en grados)
    val dLat = Math.toRadians(lat2 - lat1)
    val dLng = Math.toRadians(lng2 - lng1)
// "a" es un  valor intermedio de la formula.
    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLng / 2) * sin(dLng / 2)

    // "c" es el angulo central entre los dos puntos.
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    //Distancia = arco de circulo maximo x radio de la tierra
    return radioTierraKm * c
}