package com.example.ica_explore.core.map

import android.graphics.Color
import com.example.ica_explore.features.places.domain.model.Place
import com.google.gson.JsonObject
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.maps.Style
import org.maplibre.android.style.expressions.Expression
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.Property
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point

/**
 * Configuración del mapa de Ica (MapLibre + OpenStreetMap).
 *
 * Centraliza la cámara, los límites (para mostrar "solo Ica"), el estilo base
 * y las capas de marcadores de restaurantes y lugares turísticos.
 */
object IcaMap {

    /** Centro del mapa (Plaza de Armas de Ica). */
    val CENTER = LatLng(-14.0678, -75.7286)

    /**
     * Límites de la cámara: impide desplazarse fuera de la ciudad de Ica y
     * alrededores (Huacachina, bodegas).
     */
    val BOUNDS: LatLngBounds = LatLngBounds.Builder()
        .include(LatLng(-14.120, -75.820))
        .include(LatLng(-14.010, -75.670))
        .build()

    const val START_ZOOM = 13.0
    const val MIN_ZOOM = 12.0
    const val MAX_ZOOM = 17.5

    /** Estilo base servido desde assets (teselas ráster de OpenStreetMap). */
    const val STYLE_ASSET = "asset://mapa_ica_style.json"

    private const val SOURCE_ID = "ica-places"
    private const val CIRCLE_LAYER_ID = "ica-places-circle"
    private const val LABEL_LAYER_ID = "ica-places-label"
    private const val DEFAULT_COLOR = "#C62828"
    private const val FONT = "Open Sans Regular"

    /** Id de la capa de círculos, usado al capturar toques sobre el mapa. */
    const val CIRCLES_LAYER = CIRCLE_LAYER_ID

    /**
     * Convierte una lista de lugares al GeoJSON que consume el mapa.
     * Recuerda que GeoJSON usa el orden [longitud, latitud].
     *
     * @param places Lugares (dominio) a dibujar.
     * @param colorByCategory Color hexadecimal por id de categoría.
     */
    fun buildGeoJson(places: List<Place>, colorByCategory: Map<String, String>): String {
        val features = places.map { place ->
            val properties = JsonObject().apply {
                addProperty("title", place.name)
                addProperty("category", place.categoryId)
                addProperty("color", colorByCategory[place.categoryId] ?: DEFAULT_COLOR)
            }
            Feature.fromGeometry(Point.fromLngLat(place.longitude, place.latitude), properties)
        }
        return FeatureCollection.fromFeatures(features).toJson()
    }

    /**
     * Registra la fuente y las capas de marcadores una única vez en el estilo.
     * Círculo de color por categoría + etiqueta con el nombre del lugar.
     */
    fun installLayers(style: Style) {
        if (style.getSource(SOURCE_ID) != null) return

        style.addSource(
            GeoJsonSource(SOURCE_ID, FeatureCollection.fromFeatures(emptyList()).toJson())
        )

        style.addLayer(
            CircleLayer(CIRCLE_LAYER_ID, SOURCE_ID).withProperties(
                PropertyFactory.circleColor(Expression.toColor(Expression.get("color"))),
                PropertyFactory.circleRadius(9f),
                PropertyFactory.circleStrokeWidth(2f),
                PropertyFactory.circleStrokeColor(Color.WHITE)
            )
        )

        style.addLayer(
            SymbolLayer(LABEL_LAYER_ID, SOURCE_ID).withProperties(
                PropertyFactory.textField(Expression.get("title")),
                PropertyFactory.textSize(11f),
                PropertyFactory.textColor(Color.parseColor("#1A1A1A")),
                PropertyFactory.textHaloColor(Color.WHITE),
                PropertyFactory.textHaloWidth(1.5f),
                PropertyFactory.textOffset(arrayOf(0f, 1.1f)),
                PropertyFactory.textAnchor(Property.TEXT_ANCHOR_TOP),
                PropertyFactory.textAllowOverlap(true),
                PropertyFactory.textIgnorePlacement(true),
                PropertyFactory.textFont(arrayOf(FONT))
            )
        )
    }

    /** Actualiza en caliente los marcadores dibujados en el mapa. */
    fun updateData(style: Style, geoJson: String) {
        style.getSourceAs<GeoJsonSource>(SOURCE_ID)?.setGeoJson(geoJson)
    }
}
