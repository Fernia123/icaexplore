package com.example.ica_explore.util

import com.example.ica_explore.model.PlaceUiModel
import com.google.gson.JsonObject
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point

/**
 * Convertirá una lista de lugares (PlaceUiModel) al formato GeoJSON
 * que consume el motor de mapas (IcaMap.updateData del prototipo del líder).
 *
 * ⚠️ Importante: el estándar GeoJSON define las coordenadas como [longitud, latitud]
 * — orden inverso respecto a PlaceUiModel.
 *
 * 🌐 "Dialecto" acordado con el mapa del líder: cada feature debe llevar
 * además las propiedades "title" (nombre para el toque) y "color" (para
 * pintar el círculo). Sin "color", los pines no se dibujan.
 */
object GeoJsonConverter {

    /**
     * Traduce una categoría a su color hexadecimal.
     * Usa los mismos tonos del DemoCatalog para que todo
     * se vea como una sola app.
     */
    private fun colorDe(categoria: String?): String = when (categoria?.lowercase()) {
        "turistico", "turístico"  -> "#1E88E5"   // azul  (como "Lugares icónicos")
        "restaurante"             -> "#E53935"   // rojo  (como "Gastronomía")
        else                      -> "#C62828"   // rojo oscuro, el DEFAULT_COLOR del líder
    }

    fun convertirAGeoJson(lugares: List<PlaceUiModel>): String {

        // map {} transformará cada lugar en un Feature.
        // Una lista de N lugares entra -> sale una lista de N Features.
        val features = lugares.map { lugar ->

            // "properties" es la bolsa de datos extra que viajará con el pin.
            val propiedades = JsonObject().apply {
                addProperty("id", lugar.id)
                addProperty("title", lugar.nombre)             // ← NUEVO: el mapa lee "title" al tocar
                addProperty("nombre", lugar.nombre)             // se conserva, por si tu UI la usa
                addProperty("categoria", lugar.categoria)
                addProperty("color", colorDe(lugar.categoria))  // ← NUEVO: el círculo se pinta con esto
                addProperty("descripcion", lugar.descripcion)
            }

            // ⚠️ Aquí ocurrirá el intercambio de orden: (longitud, latitud)
            Feature.fromGeometry(
                Point.fromLngLat(lugar.longitud, lugar.latitud),
                propiedades
            )
        }

        // Se empaqueta la lista de features y la serializamos a texto JSON
        return FeatureCollection.fromFeatures(features).toJson()
    }
}