package com.example.ica_explore.domain.location

import android.location.Location
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull

class LocationTimeoutException(message: String) : Exception(message)

interface LocationProvider {
    /**
     * Flujo reactivo de la ubicacion en tiempo real del usuario en Ica.
     */
    val userLocationFlow: StateFlow<Location?>

    /**
     * Obtiene la ubicacion actual con tiempo de espera seguro.
     * @param timeoutMillis Tiempo limite en milisegundos (por defecto 5000L).
     * @return Result.success con la ubicacion o Result.failure con la causa.
     */
    suspend fun awaitUserLocation(timeoutMillis: Long = 5000L): Result<Location> {
        val location = withTimeoutOrNull(timeoutMillis) {
            userLocationFlow.filterNotNull().first()
        }
        return if (location != null) {
            Result.success(location)
        } else {
            Result.failure(LocationTimeoutException("El sensor GPS no respondio en $timeoutMillis ms o los permisos estan inactivos."))
        }
    }
}
