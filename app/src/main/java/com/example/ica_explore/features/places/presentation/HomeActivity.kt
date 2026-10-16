package com.example.ica_explore.features.places.presentation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ica_explore.R
import com.example.ica_explore.core.demo.DemoCatalog
import com.example.ica_explore.core.map.IcaMap
import com.example.ica_explore.databinding.ActivityHomeBinding
import com.example.ica_explore.databinding.ItemCategoryBinding
import com.example.ica_explore.databinding.ItemPlaceBinding
import com.example.ica_explore.di.AppContainer
import com.example.ica_explore.di.SupabaseClientProvider
import com.example.ica_explore.features.auth.presentation.LoginActivity
import com.example.ica_explore.features.categories.domain.model.Category
import com.example.ica_explore.features.places.domain.model.Place
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style

/**
 * Pantalla de inicio: muestra un mapa de Ica de fondo con las ubicaciones de
 * restaurantes y lugares turísticos, más las listas de categorías y lugares
 * populares en un panel inferior.
 *
 * Forma parte de la capa de Presentación de la feature `places`. El mapa usa
 * MapLibre + OpenStreetMap ([IcaMap]) y los datos llegan desde los casos de uso
 * del dominio (o desde [DemoCatalog] en modo demo).
 */
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    /** Indica si la pantalla se abrió en modo demo (sin autenticación real). */
    private var isDemo: Boolean = false

    private lateinit var mapView: MapView
    private var map: MapLibreMap? = null

    /** Estilo ya cargado; se usa para dibujar marcadores cuando hay datos. */
    private var mapStyle: Style? = null

    /** Últimos lugares mostrados, para pintarlos en el mapa. */
    private var currentPlaces: List<Place> = emptyList()

    /** Color por id de categoría, para pintar los marcadores. */
    private val categoryColors = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isDemo = intent.getBooleanExtra(EXTRA_DEMO, false)

        mapView = binding.mapHome
        mapView.onCreate(savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnLogout.setOnClickListener { logout() }
        setupMap()
        loadContent()
    }

    /**
     * Inicializa el mapa: cámara centrada en Ica, límites para que solo se vea
     * Ica, estilo base desde assets y capas de marcadores.
     */
    private fun setupMap() {
        mapView.getMapAsync { mapLibreMap ->
            map = mapLibreMap

            // Restringe la cámara a la ciudad de Ica ("solo Ica").
            mapLibreMap.setLatLngBoundsForCameraTarget(IcaMap.BOUNDS)
            mapLibreMap.setMinZoomPreference(IcaMap.MIN_ZOOM)
            mapLibreMap.setMaxZoomPreference(IcaMap.MAX_ZOOM)
            mapLibreMap.cameraPosition = CameraPosition.Builder()
                .target(IcaMap.CENTER)
                .zoom(IcaMap.START_ZOOM)
                .build()

            // Gestos simples: sin rotación ni inclinación.
            mapLibreMap.uiSettings.isRotateGesturesEnabled = false
            mapLibreMap.uiSettings.isTiltGesturesEnabled = false

            // Al tocar un marcador se muestra el nombre del lugar.
            mapLibreMap.addOnMapClickListener { latLng ->
                val screenPoint = mapLibreMap.projection.toScreenLocation(latLng)
                val feature = mapLibreMap
                    .queryRenderedFeatures(screenPoint, IcaMap.CIRCLES_LAYER)
                    .firstOrNull()
                val title = feature?.getStringProperty("title")
                if (title != null) {
                    Toast.makeText(this, title, Toast.LENGTH_SHORT).show()
                    true
                } else {
                    false
                }
            }

            mapLibreMap.setStyle(IcaMap.STYLE_ASSET) { style ->
                IcaMap.installLayers(style)
                mapStyle = style
                updateMapMarkers()
            }
        }
    }

    /**
     * Carga el contenido de la pantalla.
     *
     * En modo demo pinta los datos de ejemplo locales ([DemoCatalog]) sin tocar
     * la red. En modo normal consume los casos de uso que llegan a Supabase.
     */
    private fun loadContent() {
        if (isDemo) {
            binding.textDemoBanner.visibility = View.VISIBLE
            binding.textDemoBanner.text = getString(R.string.demo_banner)
            renderCategories(DemoCatalog.categories)
            renderPlaces(DemoCatalog.popularPlaces)
            return
        }

        // Si no hay credenciales no vale la pena tocar la red: evitamos la excepción.
        if (!SupabaseClientProvider.isConfigured) {
            binding.textStatus.text = getString(R.string.missing_credentials)
            return
        }

        setLoading(true)
        binding.textStatus.text = null

        lifecycleScope.launch {
            val categoriesDeferred = async { AppContainer.getCategoriesUseCase() }
            val placesDeferred = async { AppContainer.getPopularPlacesUseCase(POPULAR_LIMIT) }

            val categoriesResult = categoriesDeferred.await()
            val placesResult = placesDeferred.await()

            setLoading(false)

            if (categoriesResult.isSuccess) {
                renderCategories(categoriesResult.getOrDefault(emptyList()))
            } else {
                binding.textStatus.text =
                    categoriesResult.exceptionOrNull()?.message ?: getString(R.string.no_categories)
            }

            if (placesResult.isSuccess) {
                renderPlaces(placesResult.getOrDefault(emptyList()))
            } else {
                binding.textStatus.text =
                    placesResult.exceptionOrNull()?.message ?: getString(R.string.no_places)
            }
        }
    }

    private fun renderCategories(categories: List<Category>) {
        binding.containerCategories.removeAllViews()
        categories.forEach { category ->
            category.colorHex?.let { categoryColors[category.id] = it }
        }
        if (categories.isEmpty()) {
            binding.textCategoriesTitle.visibility = View.GONE
            return
        }
        val inflater = LayoutInflater.from(this)
        categories.forEach { category ->
            val item = ItemCategoryBinding.inflate(inflater, binding.containerCategories, false)
            item.textName.text = category.name
            item.viewDot.setBackgroundColor(parseColor(category.colorHex))
            binding.containerCategories.addView(item.root)
        }
    }

    private fun renderPlaces(places: List<Place>) {
        currentPlaces = places
        binding.containerPlaces.removeAllViews()
        updateMapMarkers()
        if (places.isEmpty()) {
            binding.textPlacesTitle.visibility = View.GONE
            return
        }
        val inflater = LayoutInflater.from(this)
        places.forEach { place ->
            val item = ItemPlaceBinding.inflate(inflater, binding.containerPlaces, false)
            item.textName.text = place.name
            item.textAddress.text = place.address ?: place.description ?: ""
            item.textRating.text = getString(
                R.string.rating_placeholder,
                place.ratingAvg,
                place.reviewsCount
            )
            // Conexión con el Módulo de Rutas: al pulsar un lugar se abre su cálculo de ruta
            item.root.setOnClickListener {
                com.example.ica_explore.features.routes.presentation.RouteActivity.start(
                    this@HomeActivity,
                    place.name,
                    place.latitude,
                    place.longitude
                )
            }
            binding.containerPlaces.addView(item.root)
        }
    }

    /**
     * Dibuja en el mapa las ubicaciones de los lugares actuales.
     * No hace nada hasta que el estilo del mapa y los datos estén listos.
     */
    private fun updateMapMarkers() {
        val style = mapStyle ?: return
        val geoJson = IcaMap.buildGeoJson(currentPlaces, categoryColors)
        IcaMap.updateData(style, geoJson)
    }

    private fun logout() {
        lifecycleScope.launch {
            runCatching { AppContainer.signOutUseCase() }
            startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            finish()
        }
    }

    private fun setLoading(loading: Boolean) {
        binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
    }

    /**
     * Convierte un color hexadecimal (ej. "#FF5722") a un color de Android.
     * Devuelve gris si el valor es nulo o inválido.
     */
    private fun parseColor(hex: String?): Int {
        return try {
            if (hex.isNullOrBlank()) return 0xFF9E9E9E.toInt()
            android.graphics.Color.parseColor(hex)
        } catch (e: IllegalArgumentException) {
            0xFF9E9E9E.toInt()
        }
    }

    // --- Ciclo de vida del MapView (requerido por MapLibre) ---

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    companion object {
        private const val POPULAR_LIMIT = 10

        /** Extra booleano que activa el modo demo (datos de ejemplo, sin Supabase). */
        const val EXTRA_DEMO = "extra_demo"
    }
}
