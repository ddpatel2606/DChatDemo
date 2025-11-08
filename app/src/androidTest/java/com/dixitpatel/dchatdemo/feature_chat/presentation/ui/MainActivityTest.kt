package com.dixitpatel.dchatdemo.feature_chat.presentation.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI test class for [MainActivity]. This class verifies that the main activity launches correctly
 * and displays the expected initial UI components. It uses Hilt for dependency injection in an
 * Android test environment.
 *
 * The tests in this class are instrumental tests that run on a device or emulator.
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    //Hilt Rule
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    //Compose Rule
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun appLaunches_andChatListScreenIsDisplayed() {
        // Check that the activity is launched and the initial composable is rendered
        composeTestRule.onNodeWithTag("chatListScreen").assertIsDisplayed()
    }
}