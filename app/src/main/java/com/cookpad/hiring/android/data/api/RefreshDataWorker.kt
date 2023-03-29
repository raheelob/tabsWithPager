package com.cookpad.hiring.android.data.api

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cookpad.hiring.android.ui.recipecollection.usecase.CollectionRecipeUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class RefreshDataWorker  @AssistedInject constructor(@Assisted appContext: Context,
                                                     @Assisted workerParams: WorkerParameters,
                                                     val collectionRecipeUseCase: CollectionRecipeUseCase): CoroutineWorker(appContext, workerParams) {

    companion object {
        const val WORK_NAME = "com.cookpad.hiring.android.data.api.RefreshDataWorker"
    }


    override suspend fun doWork(): Result {

        Log.d("","")

        try{
            collectionRecipeUseCase.execute(CollectionRecipeUseCase.Params(fetchLocal = true))
                .collect { response ->
                    when (response) {
                        RemoteData.Loading -> {
                            Log.d("","")
                        }

                        is RemoteData.Success -> response.value?.let {
                            Log.d("","")
                        }

                        is RemoteData.RemoteErrorByNetwork ->{
                            Log.d("","")
                        }

                        is RemoteData.Error -> response.error?.let {
                            Log.d("","")
                        }
                    }
                }
            return Result.success()
            // Task result
        }
        catch (throwable: Throwable){
            return Result.failure()
        }
    }

    /*override fun doWork(): Result {

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

        Log.d("WorkerClass","It's Working")
        // Task result
        return Result.success()
    }*/
}