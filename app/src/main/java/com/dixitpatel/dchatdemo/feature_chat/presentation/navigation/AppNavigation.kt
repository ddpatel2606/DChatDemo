package com.dixitpatel.dchatdemo.feature_chat.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dixitpatel.dchatdemo.feature_chat.presentation.chatlistscreen.ChatListScreen

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