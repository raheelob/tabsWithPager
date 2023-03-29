package com.cookpad.hiring.android.ui.recipecollection.fragment

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import com.cookpad.hiring.android.R
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class CollectionListFragmentTest {

    private lateinit var scenario: FragmentScenario<CollectionListFragment>

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer(themeResId = R.style.ThemeOverlay_AppCompat_DayNight)
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun testButton(){
        Espresso.onView(ViewMatchers.withId(R.id.x_right)).perform(click())
        Espresso.closeSoftKeyboard()
    }

}