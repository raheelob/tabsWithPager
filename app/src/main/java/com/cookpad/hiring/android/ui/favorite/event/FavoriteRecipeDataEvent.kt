package com.cookpad.hiring.android.ui.favorite.event

import com.cookpad.hiring.android.data.entities.CollectionEntity

sealed class FavoriteRecipeDataEvent {
    data class FavoriteList(val favoriteList: List<CollectionEntity>) : FavoriteRecipeDataEvent()
    data class UpdateFavoriteList(val updateFavoriteList: List<CollectionEntity>) : FavoriteRecipeDataEvent()
}