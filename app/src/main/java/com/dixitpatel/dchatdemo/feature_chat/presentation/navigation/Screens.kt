package com.dixitpatel.dchatdemo.feature_chat.presentation.navigation


sealed interface Screens {
    val route: String

    data object ChatListScreen : Screens {
        override val route: String = "chatListScreen"
    }
}
