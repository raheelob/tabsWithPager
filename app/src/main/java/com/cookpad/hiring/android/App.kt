package com.cookpad.hiring.android

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.cookpad.hiring.android.data.api.MyWorkerFactory
import com.cookpad.hiring.android.data.api.RefreshDataWorker
import com.cookpad.hiring.android.ui.recipecollection.usecase.CollectionRecipeUseCase
import dagger.assisted.Assisted
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    @Inject
    lateinit var collectionRecipeUseCase: CollectionRecipeUseCase

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            /*  .setWorkerFactory(workerFactory)*/
            .setWorkerFactory(MyWorkerFactory(collectionRecipeUseCase))
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        /* val repeatingRequest =
             PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.SECONDS).build()

         WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
             RefreshDataWorker.WORK_NAME,
             ExistingPeriodicWorkPolicy.KEEP,
             repeatingRequest
         )*/

        val myWorkRequest =
            PeriodicWorkRequestBuilder<RefreshDataWorker>(16, TimeUnit.MINUTES).build()

        /*WorkManager
            .getInstance(applicationContext)
            .enqueue(myWorkRequest)*/

        WorkManager
            .getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            myWorkRequest
        )
    }
}