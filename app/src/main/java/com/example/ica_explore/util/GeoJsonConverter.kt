package com.example.ica_explore.util

import com.example.ica_explore.model.PlaceUiModel
import com.google.gson.JsonObject
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point

/**
 * Se convertira una lista de lugares (PlaceUiModel) al formato GeoJSON
 * que consume el motor de mapas (MapClusterManager.updatePlacesData).
 *
 * ⚠️ Importante: el estandar GeoJSON define las cordenadas como [longitud, latitud] - orden Inverso respecto a PlaceUiModel.
 */

fun convertirAGeoJson(lugares: List<PlaceUiModel>): String {

    // map {} transformara cada lugar en un Feature.
    // Una lista de N lugares entra -> sale una lista de N Features.

    val features = lugares.map { lugar ->


        // "properties" es la bolsa de datos extra que viajara con el pin

        val propiedades = JsonObject()
        propiedades.addProperty("id", lugar.id)
        propiedades.addProperty("nombre", lugar.nombre)
        propiedades.addProperty("categoria", lugar.categoria)
        propiedades.addProperty("descripcion", lugar.descripcion)

        // ⚠️ Aqui ocurrira el intercambio de orden: (longitud, latitud)
        Feature.fromGeometry(
            Point.fromLngLat(lugar.longitud, lugar.latitud),
            propiedades
        )
    }

    // Se empaqueta la lista de features y la serializamos a texto JSON
    return FeatureCollection.fromFeatures(features).toJson()
}