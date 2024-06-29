package com.avsaverage.fooddeliveryapp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class ComposeUiTest {


    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testIfTextFieldIsDisplayed() {
        composeRule.onNodeWithTag("textField1").assertIsDisplayed()
    }
}