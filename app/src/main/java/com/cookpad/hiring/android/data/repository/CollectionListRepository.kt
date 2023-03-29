package com.cookpad.hiring.android.data.repository

import com.cookpad.hiring.android.data.api.RemoteData
import com.cookpad.hiring.android.data.entities.CollectionEntity
import kotlinx.coroutines.flow.Flow

interface CollectionListRepository{
    fun getCollectionList(fetchLocal: Boolean): Flow<RemoteData<List<CollectionEntity>>>
    fun updateSingleCollection(data: CollectionEntity) : Flow<Unit>
    fun getFavoriteCollection() : Flow<List<CollectionEntity>>
}
