package com.example.ica_explore.presentation.map

import android.graphics.Color
import org.maplibre.android.maps.Style
import org.maplibre.android.style.expressions.Expression
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.PropertyFactory
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonOptions
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.FeatureCollection

object MapClusterManager {

    private const val SOURCE_ID = "places-source"
    private const val CLUSTERS_LAYER_ID = "clusters-layer"
    private const val COUNT_LAYER_ID = "cluster-count-layer"
    private const val UNCLUSTERED_LAYER_ID = "unclustered-layer"

    /**
     * Registra el contenedor vacio y las capas visuales en la GPU una sola vez.
     */
    fun setupClusterLayers(style: Style) {
        if (style.getSource(SOURCE_ID) != null) return

        val initialEmptySource = GeoJsonSource(
            SOURCE_ID,
            FeatureCollection.fromFeatures(emptyList()).toJson(),
            GeoJsonOptions()
                .withCluster(true)
                .withClusterMaxZoom(14)
                .withClusterRadius(50)
        )
        style.addSource(initialEmptySource)

        style.addLayer(
            CircleLayer(CLUSTERS_LAYER_ID, SOURCE_ID).apply {
                setFilter(Expression.has("point_count"))
                setProperties(
                    PropertyFactory.circleColor(Color.parseColor("#800020")),
                    PropertyFactory.circleRadius(20f),
                    PropertyFactory.circleStrokeWidth(2f),
                    PropertyFactory.circleStrokeColor(Color.WHITE)
                )
            }
        )

        style.addLayer(
            SymbolLayer(COUNT_LAYER_ID, SOURCE_ID).apply {
                setFilter(Expression.has("point_count"))
                setProperties(
                    PropertyFactory.textField(Expression.toString(Expression.get("point_count"))),
                    PropertyFactory.textSize(12f),
                    PropertyFactory.textColor(Color.WHITE),
                    PropertyFactory.textIgnorePlacement(true),
                    PropertyFactory.textAllowOverlap(true)
                )
            }
        )

        style.addLayer(
            SymbolLayer(UNCLUSTERED_LAYER_ID, SOURCE_ID).apply {
                setFilter(Expression.not(Expression.has("point_count")))
                setProperties(
                    PropertyFactory.iconImage("marker-icon-default"),
                    PropertyFactory.iconAllowOverlap(true)
                )
            }
        )
    }

    /**
     * Actualiza la fuente existente en caliente.
     */
    fun updatePlacesData(style: Style, geoJsonString: String) {
        val source = style.getSourceAs<GeoJsonSource>(SOURCE_ID)
        source?.setGeoJson(geoJsonString)
    }
}
