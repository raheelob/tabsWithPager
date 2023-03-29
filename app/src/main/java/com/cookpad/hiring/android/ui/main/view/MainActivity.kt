package com.cookpad.hiring.android.ui.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.work.*
import com.cookpad.hiring.android.App
import com.cookpad.hiring.android.R
import com.cookpad.hiring.android.data.api.RefreshDataWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val defaultHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        val navController = (defaultHostFragment as NavHostFragment).navController
        NavigationUI.setupActionBarWithNavController(this, navController)
        /*val EXPIRY_CHECK = "unique_worker_name"

        val myWorkRequest: WorkRequest =
            PeriodicWorkRequestBuilder<RefreshDataWorker>(16, TimeUnit.MINUTES).build()

        WorkManager
            .getInstance(applicationContext)
            .enqueue(myWorkRequest)*/

        /*val myWorkRequest =
            PeriodicWorkRequestBuilder<RefreshDataWorker>(16, TimeUnit.MINUTES).build()

        WorkManager
            .getInstance(applicationContext)
            .enqueue(myWorkRequest)*/
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, AppBarConfiguration(navController.graph))
    }

}