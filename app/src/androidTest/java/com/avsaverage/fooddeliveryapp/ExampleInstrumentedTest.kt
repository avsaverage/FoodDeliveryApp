package com.avsaverage.fooddeliveryapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.avsaverage.fooddeliveryapp.ui.theme.FoodDeliveryAppTheme

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.avsaverage.fooddeliveryapp", appContext.packageName)
    }

    @Test
    fun checkActivityLaunch() {
        // Launch the main activity
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        // Verify that the activity is launched
        activityScenario.onActivity { activity ->
            assertNotNull(activity)
        }

        // Close the activity scenario
        activityScenario.close()
    }

    @Test
    fun checkNavigationToCartFragment() {
        // Create a test environment for Compose
        val composeTestRule = createComposeRule()

        // Launch the main composable
        composeTestRule.setContent {
            FoodDeliveryAppTheme {
                val navController = rememberNavController()
            }
        }

        // Click on the home navigation item
        composeTestRule.onNodeWithContentDescription("Меню").performClick()

        // Verify that the home screen is displayed
        composeTestRule.onNodeWithText("Меню").assertIsDisplayed()
    }
}