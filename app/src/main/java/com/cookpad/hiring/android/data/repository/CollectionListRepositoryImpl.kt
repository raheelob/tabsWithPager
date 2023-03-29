package com.cookpad.hiring.android.data.repository

import com.cookpad.hiring.android.data.api.CookpadHiringService
import com.cookpad.hiring.android.data.api.RemoteData
import com.cookpad.hiring.android.data.entities.CollectionEntity
import com.cookpad.hiring.android.data.local.RecipeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class CollectionListRepositoryImpl @javax.inject.Inject constructor(
    private val api: CookpadHiringService,
    private val dao: RecipeDao
) : CollectionListRepository {

    override fun getCollectionList(fetchLocal: Boolean): Flow<RemoteData<List<CollectionEntity>>> =
        flow<RemoteData<List<CollectionEntity>>> {
            if (fetchLocal) {
                val response = dao.getAllCollection()
                emit(RemoteData.Success(response))
            } else {
                val favoriteList = dao.getFavoriteCollection()
                val remoteList = api.getCollections().map { collectionDTO ->
                    with(collectionDTO) {
                        val isFavorite = favoriteList.any { it.id == this.id }
                        CollectionEntity(
                            id = id,
                            title = title,
                            description = description,
                            recipeCount = recipeCount,
                            previewImageUrls = previewImageUrls,
                            isFavorite = isFavorite
                        )
                    }
                }
                dao.insertAllCollection(remoteList) // update local list...
                emit(RemoteData.Success(remoteList))
            }
        }.flowOn(Dispatchers.IO)

    override fun updateSingleCollection(data: CollectionEntity) = flow<Unit> {
        dao.insertSingleCollection(data)
    }.flowOn(Dispatchers.IO)

    override fun getFavoriteCollection() = flow {
        emit(dao.getFavoriteCollection())
    }.flowOn(Dispatchers.IO)

}