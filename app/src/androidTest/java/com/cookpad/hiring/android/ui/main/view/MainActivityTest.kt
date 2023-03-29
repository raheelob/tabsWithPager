package com.cookpad.hiring.android.ui.main.view

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.core.app.ActivityScenario.launch

@RunWith(AndroidJUnit4::class)
class MainActivityTest{

    @get:Rule
    var activityScenarioRule = activityScenarioRule<MainActivity>()
   /* private lateinit var scenario : ActivityScenario<MainActivity>*/

    @Before
    fun setUp(){
        val scenario = activityScenarioRule.scenario
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun testEvent() {
       launch(MainActivity::class.java).use {
           it.moveToState(Lifecycle.State.RESUMED)
       }
    }
}