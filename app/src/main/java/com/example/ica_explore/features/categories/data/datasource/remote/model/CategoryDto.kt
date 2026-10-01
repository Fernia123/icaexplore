package com.example.ica_explore.features.categories.data.datasource.remote.model

import com.example.ica_explore.features.categories.domain.model.Category
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: String,
    val name: String,
    val slug: String,
    @SerialName("icon_name") val iconName: String?,
    @SerialName("color_hex") val colorHex: String?,
    @SerialName("sort_order") val sortOrder: Int,
    @SerialName("is_active") val isActive: Boolean
) {
    
}
