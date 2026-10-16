package com.example.ica_explore.features.routes.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.ica_explore.R
import com.example.ica_explore.databinding.ActivityRouteBinding
import com.example.ica_explore.features.routes.data.local.RouteDestination
import com.example.ica_explore.features.routes.data.local.RoutePlacesCatalog
import com.example.ica_explore.features.routes.domain.usecase.GetRouteUseCase
import com.example.ica_explore.features.routes.domain.util.HaversineDistanceCalculator
import com.example.ica_explore.features.routes.presentation.util.NavigationFallback
import com.example.ica_explore.features.routes.presentation.util.TimeDistanceFormatter
import java.util.Locale
import kotlinx.coroutines.launch

/**
 * Pantalla dedicada del Módulo de Rutas:
 * Permite calcular distancias y tiempos de viaje hacia los 45 atractivos de Ica,
 * simular modos de transporte (Auto, Caminata, Bicicleta) mediante OpenRouteService
 * y lanzar la navegación guiada por GPS en Google Maps o Waze.
 */
class RouteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRouteBinding
    private val getRouteUseCase = GetRouteUseCase()

    private val originLat = -14.0678
    private val originLng = -75.7286

    private var currentDestination: RouteDestination = RoutePlacesCatalog.destinations.first()
    private var currentProfile: String = "driving-car"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRouteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupDestinationsSpinner()
        setupModeChips()
        setupActions()

        // Si se abrió desde otra pantalla con destino específico, seleccionarlo
        val targetName = intent.getStringExtra(EXTRA_DEST_NAME)
        if (!targetName.isNullOrBlank()) {
            val idx = RoutePlacesCatalog.destinations.indexOfFirst {
                it.name.equals(targetName, ignoreCase = true)
            }
            if (idx >= 0) {
                binding.spinnerDestinations.setSelection(idx)
            }
        }
    }

    private fun setupToolbar() {
        binding.toolbarRoute.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupDestinationsSpinner() {
        val names = RoutePlacesCatalog.destinations.map { "${it.name} (${it.categoryName.take(15)})" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, names)
        binding.spinnerDestinations.adapter = adapter

        binding.spinnerDestinations.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                currentDestination = RoutePlacesCatalog.destinations[position]
                displayDestinationDetails(currentDestination)
                calculateRoute()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupModeChips() {
        binding.chipGroupMode.setOnCheckedStateChangeListener { _, checkedIds ->
            currentProfile = when (checkedIds.firstOrNull()) {
                R.id.chipWalking -> "foot-walking"
                R.id.chipCycling -> "cycling-regular"
                else -> "driving-car"
            }
            calculateRoute()
        }
    }

    private fun setupActions() {
        binding.btnRecalculateRoute.setOnClickListener {
            calculateRoute()
        }

        binding.btnOpenNavigation.setOnClickListener {
            NavigationFallback.openExternalMap(
                context = this,
                destLat = currentDestination.latitude,
                destLng = currentDestination.longitude
            )
        }
    }

    private fun displayDestinationDetails(dest: RouteDestination) {
        binding.textCategoryBadge.text = dest.categoryName
        binding.textScheduleBadge.text = dest.scheduleStatus
        binding.textAccessibleBadge.visibility = if (dest.isAccessible) View.VISIBLE else View.GONE
        binding.textAddress.text = "📍 ${dest.address}"
        binding.textReviewQuote.text = "💬 “${dest.topReview}”"
    }

    private fun calculateRoute() {
        // 1. Cálculo local offline inmediato con fórmula Haversine
        val distKm = HaversineDistanceCalculator.calculateDistanceKm(
            originLat,
            originLng,
            currentDestination.latitude,
            currentDestination.longitude
        )

        val estimatedMins = when (currentProfile) {
            "foot-walking" -> maxOf(5, (distKm * 13.0).toInt())
            "cycling-regular" -> maxOf(4, (distKm * 4.5).toInt())
            else -> maxOf(3, (distKm * 2.2).toInt())
        }

        binding.textHaversineDistance.text = String.format(Locale.US, "%.1f km", distKm)
        binding.textEstimatedTime.text = "~$estimatedMins min"
        binding.textOrsSummary.text = "Calculando ruta en tiempo real con OpenRouteService..."
        binding.progressRoute.visibility = View.VISIBLE

        // 2. Cálculo online con OpenRouteService
        lifecycleScope.launch {
            val result = getRouteUseCase(
                profile = currentProfile,
                originLat = originLat,
                originLng = originLng,
                destLat = currentDestination.latitude,
                destLng = currentDestination.longitude
            )

            binding.progressRoute.visibility = View.GONE

            result.onSuccess { response ->
                val summary = response.routes.firstOrNull()?.summary
                if (summary != null) {
                    val formattedDist = TimeDistanceFormatter.formatDistance(summary.distance)
                    val formattedDuration = TimeDistanceFormatter.formatDuration(summary.duration)
                    binding.textHaversineDistance.text = formattedDist
                    binding.textEstimatedTime.text = formattedDuration
                    binding.textOrsSummary.text = "✅ Ruta óptima calculada por calles de Ica (ORS API)."
                } else {
                    binding.textOrsSummary.text = "Distancia estimada por línea recta (Haversine)."
                }
            }.onFailure {
                binding.textOrsSummary.text = "Modo offline: Distancia Haversine (usa Iniciar Navegación para GPS)."
            }
        }
    }

    companion object {
        const val EXTRA_DEST_NAME = "extra_dest_name"
        const val EXTRA_DEST_LAT = "extra_dest_lat"
        const val EXTRA_DEST_LNG = "extra_dest_lng"

        fun start(context: Context, name: String, lat: Double, lng: Double) {
            val intent = Intent(context, RouteActivity::class.java).apply {
                putExtra(EXTRA_DEST_NAME, name)
                putExtra(EXTRA_DEST_LAT, lat)
                putExtra(EXTRA_DEST_LNG, lng)
            }
            context.startActivity(intent)
        }
    }
}
