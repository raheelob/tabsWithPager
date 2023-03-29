package com.cookpad.hiring.android.ui.main.usecase

import com.cookpad.hiring.android.data.entities.CollectionEntity
import com.cookpad.hiring.android.data.repository.CollectionListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDbUseCase @Inject constructor(private val collectionListRepository: CollectionListRepository) {

    fun upDateRecipe(data: CollectionEntity): Flow<Unit> {
        return collectionListRepository.updateSingleCollection(data)
    }

    fun getFavoriteCollection(): Flow<List<CollectionEntity>> {
        return collectionListRepository.getFavoriteCollection()
    }

}