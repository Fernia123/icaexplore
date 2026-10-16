package com.example.ica_explore.features.routes.presentation.util

import android.content.Context
import android.content.Intent
import android.net.Uri

object NavigationFallback {

    fun openExternalMap(context: Context, destLat: Double, destLng: Double) {
        val gmmIntentUri = Uri.parse("google.navigation:q=$destLat,$destLng")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri).apply {
            setPackage("com.google.android.apps.maps")
        }

        if (mapIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(mapIntent)
        } else {
            val webUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$destLat,$destLng")
            context.startActivity(Intent(Intent.ACTION_VIEW, webUri))
        }
    }
}
