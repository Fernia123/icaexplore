package com.example.ica_explore.features.places.data.datasource.remote

import com.example.ica_explore.features.places.data.datasource.remote.model.PlaceDto

/**
 * Implementación de la fuente de datos que se conecta a Supabase.
 * Aquí inyectarás el cliente de Supabase (ej. io.github.jan-tennert.supabase:postgrest)
 */
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns

class PlaceRemoteDataSourceImpl(
    private val supabaseClient: SupabaseClient
) : PlaceRemoteDataSource {

    override suspend fun getPlacesByCategory(categoryId: String): List<PlaceDto> {
        // Ejemplo usando supabase-kt:
        // return supabaseClient.postgrest["places"]
        //     .select { filter { eq("category_id", categoryId) } }
        //     .decodeList<PlaceDto>()
        return supabaseClient.postgrest["places"]
            .select() { filter { eq("category_id", categoryId) } }
            .decodeList<PlaceDto>()
    }

    override suspend fun getPlaceById(id: String): PlaceDto? {
        return supabaseClient.postgrest["places"]
            .select() { filter { eq("id", id) } }
            .decodeSingleOrNull<PlaceDto>()
    }

    override suspend fun getPopularPlaces(limit: Int): List<PlaceDto> {
        return supabaseClient.postgrest["places"]
            .select() { order("popularity_idx", io.github.jan.supabase.postgrest.query.Order.DESCENDING); limit(limit.toLong()) }
            .decodeList<PlaceDto>()
    }
}

