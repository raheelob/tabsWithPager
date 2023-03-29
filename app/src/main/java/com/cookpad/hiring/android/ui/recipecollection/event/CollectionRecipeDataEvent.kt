package com.cookpad.hiring.android.ui.recipecollection.event

import com.cookpad.hiring.android.data.entities.CollectionEntity
import com.cookpad.hiring.android.data.model.ErrorData

sealed class CollectionRecipeDataEvent {

    object RemoteErrorByNetwork : CollectionRecipeDataEvent()
    object Loading : CollectionRecipeDataEvent()

    class Error(val errorData: ErrorData) : CollectionRecipeDataEvent()

    data class RecipeCollectionList(val recipeCollectionList: List<CollectionEntity>) : CollectionRecipeDataEvent()
    data class UpdateCollectionList(val recipeCollectionList: List<CollectionEntity>) : CollectionRecipeDataEvent()
    data class FavoriteRemovedFromTheList(val collectionEntity: CollectionEntity) : CollectionRecipeDataEvent()
}
