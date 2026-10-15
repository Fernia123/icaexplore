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

/**
 * Pantalla de inicio: lista las categorías y los lugares populares.
 * Forma parte de la capa de Presentación de la feature `places`.
 *
 * Usa los casos de uso de dominio (`GetCategoriesUseCase` y
 * `GetPopularPlacesUseCase`) sin conocer cómo se obtienen los datos.
 */
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    /** Indica si la pantalla se abrió en modo demo (sin autenticación real). */
    private var isDemo: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isDemo = intent.getBooleanExtra(EXTRA_DEMO, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnLogout.setOnClickListener { logout() }
        loadContent()
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
        binding.containerPlaces.removeAllViews()
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
            binding.containerPlaces.addView(item.root)
        }
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

    companion object {
        private const val POPULAR_LIMIT = 10

        /** Extra booleano que activa el modo demo (datos de ejemplo, sin Supabase). */
        const val EXTRA_DEMO = "extra_demo"
    }
}
