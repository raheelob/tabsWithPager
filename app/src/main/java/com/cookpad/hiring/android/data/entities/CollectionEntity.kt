package com.cookpad.hiring.android.data.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import org.jetbrains.annotations.NotNull

@Entity
@Parcelize
data class CollectionEntity(
    var isFavorite: Boolean = false,
    @PrimaryKey
    @NotNull
    @SerializedName("id"                 ) var id               : Int?              = null,
    @SerializedName("title"              ) var title            : String?           = null,
    @SerializedName("description"        ) var description      : String?           = null,
    @SerializedName("recipe_count"       ) var recipeCount      : Int?              = null,
    @SerializedName("preview_image_urls" ) var previewImageUrls : List<String> = emptyList())
    : Parcelable
{
    constructor() : this(false,0,"","", 0, emptyList()) // This is created for room...
}
