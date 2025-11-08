package com.dixitpatel.dchatdemo.feature_chat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dixitpatel.dchatdemo.feature_chat.presentation.chatlistscreen.ChatListScreen

/**
 * Composable function that sets up the navigation graph for the application.
 *
 * It uses a `NavHost` to define the navigation routes and the composable associated with them.
 * The starting destination is set to the `ChatListScreen`.
 */
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination = Screens.ChatListScreen.route
    ) {
        composable(Screens.ChatListScreen.route) {
            ChatListScreen()
        }
    }
}