package com.cookpad.hiring.android.data.dtos

import com.google.gson.annotations.SerializedName


data class CollectionDTO(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("recipe_count") var recipeCount: Int? = null,
    @SerializedName("preview_image_urls") var previewImageUrls: List<String> = emptyList()
)
