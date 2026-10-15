package com.example.ica_explore.features.auth.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.ica_explore.R
import com.example.ica_explore.databinding.ActivityLoginBinding
import com.example.ica_explore.di.AppContainer
import com.example.ica_explore.features.places.presentation.HomeActivity
import kotlinx.coroutines.launch

/**
 * Pantalla de autenticación (registro e inicio de sesión).
 * Forma parte de la capa de Presentación de la feature `auth`.
 *
 * Solo orquesta la UI: toma los datos del usuario, invoca los casos de uso
 * del dominio y pinta el resultado. No contiene lógica de negocio.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnLogin.setOnClickListener { authenticate(register = false) }
        binding.btnRegister.setOnClickListener { authenticate(register = true) }
        binding.btnDemo.setOnClickListener { startDemo() }
    }

    /**
     * Abre la pantalla principal en modo demo, sin autenticación ni Supabase.
     * Muestra los datos de ejemplo definidos en [com.example.ica_explore.core.demo.DemoCatalog].
     */
    private fun startDemo() {
        startActivity(
            Intent(this, HomeActivity::class.java)
                .putExtra(HomeActivity.EXTRA_DEMO, true)
        )
        finish()
    }

    /**
     * Ejecuta el caso de uso de registro o de inicio de sesión.
     *
     * @param register `true` para registrarse, `false` para iniciar sesión.
     */
    private fun authenticate(register: Boolean) {
        val email = binding.editEmail.text?.toString().orEmpty().trim()
        val password = binding.editPassword.text?.toString().orEmpty()

        if (email.isBlank() || password.isBlank()) {
            binding.textStatus.text = getString(R.string.empty_credentials)
            return
        }

        hideKeyboard()
        setLoading(true)
        binding.textStatus.text = null

        lifecycleScope.launch {
            // Envolver el acceso al contenedor en runCatching evita que la app se
            // cierre si faltan credenciales de Supabase al crear el cliente.
            val callResult = runCatching {
                if (register) {
                    AppContainer.signUpUseCase(email, password)
                } else {
                    AppContainer.signInUseCase(email, password)
                }
            }
            setLoading(false)

            // callResult es un Result<Result<User>>: primero validamos el acceso al
            // contenedor y luego el resultado real del caso de uso.
            callResult.fold(
                onSuccess = { useCaseResult ->
                    useCaseResult.fold(
                        onSuccess = {
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finish()
                        },
                        onFailure = { error -> showError(error) }
                    )
                },
                onFailure = { error -> showError(error) }
            )
        }
    }

    private fun showError(error: Throwable) {
        binding.textStatus.text = error.message ?: error::class.java.simpleName
    }

    /**
     * Muestra u oculta el indicador de carga y habilita los controles.
     */
    private fun setLoading(loading: Boolean) {
        binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnLogin.isEnabled = !loading
        binding.btnRegister.isEnabled = !loading
        binding.editEmail.isEnabled = !loading
        binding.editPassword.isEnabled = !loading
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
}
