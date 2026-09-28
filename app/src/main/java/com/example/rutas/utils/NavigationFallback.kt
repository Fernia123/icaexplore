package com.example.rutas.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object NavigationFallback {
    /**
     * Invocación de emergencia a Google Maps si falla openrouteservice
     */
    fun openExternalMap(context: Context, destLat: Double, destLng: Double) {
        val gmmIntentUri = Uri.parse("google.navigation:q=$destLat,$destLng")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage("com.google.android.apps.maps")
        }

        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            // Alternativa en navegador si la app no está instalada
            val webUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$destLat,$destLng")
            context.startActivity(Intent(Intent.ACTION_VIEW, webUri))
        }
    }
}