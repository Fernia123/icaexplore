package com.example.ica_explore.features.routes.presentation.util

import java.util.Locale

object TimeDistanceFormatter {

    fun formatDistance(meters: Double): String {
        return if (meters < 1000.0) {
            "${meters.toInt()} m"
        } else {
            val km = meters / 1000.0
            String.format(Locale.US, "%.1f km", km)
        }
    }

    fun formatDuration(seconds: Double): String {
        val totalMinutes = (seconds / 60.0).toInt()
        return when {
            totalMinutes < 1 -> "Menos de 1 min"
            totalMinutes < 60 -> "$totalMinutes min"
            else -> {
                val hours = totalMinutes / 60
                val remainingMinutes = totalMinutes % 60
                if (remainingMinutes == 0) "$hours h" else "$hours h $remainingMinutes min"
            }
        }
    }
}
