package com.example.ica_explore.features.categories.data.datasource.remote

import com.example.ica_explore.features.categories.data.datasource.remote.model.CategoryDto
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order

class CategoryRemoteDataSourceImpl(
    private val supabaseClient: SupabaseClient
) : CategoryRemoteDataSource {

    override suspend fun getCategories(): List<CategoryDto> {
        // Pedimos solo las categorías activas y ordenadas por 'sort_order'
        return supabaseClient.postgrest["categories"]
            .select() {
                filter { eq("is_active", true) }
                order("sort_order", Order.ASCENDING)
            }
            .decodeList<CategoryDto>()
    }
}
