package com.cookpad.hiring.android.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cookpad.hiring.android.data.api.RemoteData
import com.cookpad.hiring.android.data.entities.CollectionEntity
import com.cookpad.hiring.android.data.model.ErrorData
import com.cookpad.hiring.android.ui.favorite.event.FavoriteRecipeDataEvent
import com.cookpad.hiring.android.ui.main.usecase.LocalDbUseCase
import com.cookpad.hiring.android.ui.recipecollection.event.CollectionRecipeDataEvent
import com.cookpad.hiring.android.ui.recipecollection.usecase.CollectionRecipeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val collectionRecipeUseCase: CollectionRecipeUseCase,
    private val localDbUseCase: LocalDbUseCase
) : ViewModel() {

    val result : LiveData<String> = MutableLiveData<String>()
    var mList: ArrayList<String> = ArrayList<String>()



    //remote // Collection operation...
    /***********************************************************************************/
    private val discoverCollectionTasksEventChannel = Channel<CollectionRecipeDataEvent>()
    internal val discoverCollectionTasksEvent = discoverCollectionTasksEventChannel.receiveAsFlow()
    /***********************************************************************************/

    //remote // Favorite operation...
    /***********************************************************************************/
    private val favoriteTasksEventChannel = Channel<FavoriteRecipeDataEvent>()
    internal val favoriteTasksEvent = favoriteTasksEventChannel.receiveAsFlow()
    /***********************************************************************************/

    init {
        loadCollections(fetchFromLocal = false)
        for(i in mList.indices){
          println(mList[i])
        }

        mList.forEach {

        }

    }

    fun refresh() {
        loadCollections(fetchFromLocal = false)
    }

    internal fun fetchFromLocal(fetchFromLocal: Boolean) {
        loadCollections(fetchFromLocal = fetchFromLocal)
    }

    internal fun getFavoriteList() = viewModelScope.launch {
        localDbUseCase.getFavoriteCollection().collect{ favoriteList->
            sendFavoriteList(favoriteList)
        }
    }

    private fun loadCollections(fetchFromLocal: Boolean) = viewModelScope.launch {
        discoverCollectionTasksEventChannel.send(CollectionRecipeDataEvent.Loading)
        collectionRecipeUseCase.execute(CollectionRecipeUseCase.Params(fetchLocal = fetchFromLocal))
            .collect { response ->
                when (response) {
                    RemoteData.Loading -> sendLoadingEvent()

                    is RemoteData.Success -> response.value?.let {
                        getCollectionListEvent(it)
                    }

                    is RemoteData.RemoteErrorByNetwork -> sendRemoteErrorByNetworkEvent()

                    is RemoteData.Error -> response.error?.let { sendErrorEvent(it) }
                }
            }
    }

    private suspend fun sendLoadingEvent() {
        discoverCollectionTasksEventChannel.send(CollectionRecipeDataEvent.Loading)
    }

    private suspend fun sendRemoteErrorByNetworkEvent() {
        discoverCollectionTasksEventChannel.send(CollectionRecipeDataEvent.RemoteErrorByNetwork)
    }

    internal suspend fun sendErrorEvent(errorData: ErrorData) {
        discoverCollectionTasksEventChannel.send(CollectionRecipeDataEvent.Error(errorData))
    }

    internal suspend fun sendFavoriteRemoveFromTheList(collectionEntity: CollectionEntity) {
        discoverCollectionTasksEventChannel.send(CollectionRecipeDataEvent.FavoriteRemovedFromTheList(collectionEntity))
    }

    internal suspend fun getCollectionListEvent(recipeCollectionList: List<CollectionEntity>) {
        if (recipeCollectionList.isNotEmpty()) {
            discoverCollectionTasksEventChannel.send(
                CollectionRecipeDataEvent.RecipeCollectionList(
                    recipeCollectionList = recipeCollectionList
                )
            )
        } else
            sendErrorEvent(ErrorData(ok = false, errorCode = 1, error = "Collection List is Empty"))
    }

    internal suspend fun updateCollectionListEvent(recipeCollectionList: List<CollectionEntity>) {
        if (recipeCollectionList.isNotEmpty()) {
            discoverCollectionTasksEventChannel.send(
                CollectionRecipeDataEvent.UpdateCollectionList(
                    recipeCollectionList = recipeCollectionList
                )
            )
        } else
            sendErrorEvent(ErrorData(ok = false, errorCode = 1, error = "Collection List is Empty"))
    }

    internal suspend fun sendFavoriteList(favoriteList: List<CollectionEntity>){
        favoriteTasksEventChannel.send(
            FavoriteRecipeDataEvent.FavoriteList(
                favoriteList = favoriteList
            )
        )
    }

    internal suspend fun sendUpdatedFavoriteList(favoriteList: List<CollectionEntity>){
        favoriteTasksEventChannel.send(
            FavoriteRecipeDataEvent.UpdateFavoriteList(
                 updateFavoriteList = favoriteList
            )
        )
    }

    internal fun whenFavoriteClick(mList : MutableList<CollectionEntity>, position: Int, recipe : CollectionEntity){
       viewModelScope.launch {
           val updateRecipe = consolidateRecipeEntity(recipe)
           mList[position] = updateRecipe
           callUpdateCollection(mList)
           updateSingleCollectionInDB(updateRecipe)
       }
    }

    private fun updateSingleCollectionInDB(updateRecipe : CollectionEntity) = viewModelScope.launch {
        localDbUseCase.upDateRecipe(updateRecipe).collect()
    }

    internal fun consolidateRecipeEntity(recipe : CollectionEntity) =  CollectionEntity(
        isFavorite = !recipe.isFavorite,
        id = recipe.id,
        title = recipe.title,
        description = recipe.description,
        recipeCount = recipe.recipeCount,
        previewImageUrls = recipe.previewImageUrls
    )

    private fun callUpdateCollection(mList : MutableList<CollectionEntity>) = viewModelScope.launch {
        updateCollectionListEvent(mList)
    }

    internal fun removeFavoriteFromFavoriteList(mList : MutableList<CollectionEntity>, recipe : CollectionEntity){
        viewModelScope.launch {
            //remove item from favorite list...
            mList.remove(recipe)
            sendUpdatedFavoriteList(mList)
            //change value of favorite...
            val updateRecipe = consolidateRecipeEntity(recipe)
            updateSingleCollectionInDB(updateRecipe)
            sendFavoriteRemoveFromTheList(collectionEntity = recipe)
        }
    }


}

