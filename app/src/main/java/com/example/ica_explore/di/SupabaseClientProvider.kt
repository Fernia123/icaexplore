package com.example.ica_explore.di

import com.example.ica_explore.BuildConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

/**
 * Proveedor del cliente de Supabase.
 *
 * Lee las credenciales desde BuildConfig (que se generan a partir de
 * local.properties) e inicializa los plugins de Auth y Postgrest.
 * La creación es perezosa (lazy) para no inicializar la red si no se usa.
 */
object SupabaseClientProvider {

    /**
     * Indica si se definieron las credenciales en local.properties.
     * Permite a la UI avisar al usuario sin lanzar una excepción.
     */
    val isConfigured: Boolean
        get() = BuildConfig.SUPABASE_URL.isNotBlank() && BuildConfig.SUPABASE_ANON_KEY.isNotBlank()

    /**
     * Cliente único (singleton) de Supabase para toda la aplicación.
     */
    val client: SupabaseClient by lazy { createClient() }

    /**
     * Construye el cliente de Supabase validando que las credenciales existan.
     * Lanza una excepción clara si faltan SUPABASE_URL o SUPABASE_ANON_KEY
     * en el archivo local.properties.
     */
    private fun createClient(): SupabaseClient {
        val supabaseUrl = BuildConfig.SUPABASE_URL
        val supabaseKey = BuildConfig.SUPABASE_ANON_KEY

        require(supabaseUrl.isNotBlank() && supabaseKey.isNotBlank()) {
            "Faltan credenciales: define SUPABASE_URL y SUPABASE_ANON_KEY en local.properties"
        }

        return createSupabaseClient(
            supabaseUrl = supabaseUrl,
            supabaseKey = supabaseKey
        ) {
            install(Auth)
            install(Postgrest)
        }
    }
}
