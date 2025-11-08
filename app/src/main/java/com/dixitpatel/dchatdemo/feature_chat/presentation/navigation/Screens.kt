package com.dixitpatel.dchatdemo.feature_chat.presentation.navigation


/**
 * Defines all the screens in the app for navigation purposes.
 * Each screen is represented by a data object that implements this interface.
 *
 * @property route The unique string identifier for the navigation route.
 */
sealed interface Screens {
    val route: String

    data object ChatListScreen : Screens {
        override val route: String = "chatListScreen"
    }
}
