package com.cookpad.hiring.android.data.api

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.cookpad.hiring.android.ui.recipecollection.usecase.CollectionRecipeUseCase

class MyWorkerFactory(private val service: CollectionRecipeUseCase) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        // This only handles a single Worker, please don’t do this!!
        // See below for a better way using DelegatingWorkerFactory
        return RefreshDataWorker(appContext, workerParameters, service)

    }
}