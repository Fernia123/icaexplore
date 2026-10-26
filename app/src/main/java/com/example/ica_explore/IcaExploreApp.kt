package com.example.ica_explore

import android.app.Application
import org.maplibre.android.MapLibre

/**
 * Clase Application de Ica-Explore.
 *
 * Inicializa MapLibre una sola vez, antes de que cualquier pantalla infle un
 * `MapView`. Es un requisito del SDK: `MapLibre.getInstance()` debe llamarse
 * antes de crear la vista del mapa.
 */
class IcaExploreApp : Application() {

    override fun onCreate() {
        super.onCreate()
        MapLibre.getInstance(this)
    }
}
