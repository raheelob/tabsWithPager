package com.cookpad.hiring.android.ui.recipecollection.usecase

import com.cookpad.hiring.android.data.api.RemoteData
import com.cookpad.hiring.android.data.api.UseCaseExecutor
import com.cookpad.hiring.android.data.entities.CollectionEntity
import com.cookpad.hiring.android.data.repository.CollectionListRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CollectionRecipeUseCase @Inject constructor(private val collectionListRepository: CollectionListRepository) :
    UseCaseExecutor<CollectionRecipeUseCase.Params, List<CollectionEntity>>() {
    override fun runUseCase(parameter: Params?): Flow<RemoteData<List<CollectionEntity>>> {
        return collectionListRepository.getCollectionList(fetchLocal = parameter?.fetchLocal ?: false)
    }

    data class Params constructor(
        val fetchLocal: Boolean
    ) {
        companion object {
            fun create(
                fetchLocal: Boolean
            ) = Params(fetchLocal)
        }
    }
}