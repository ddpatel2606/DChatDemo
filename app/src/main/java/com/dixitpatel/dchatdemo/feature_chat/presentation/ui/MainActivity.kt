package com.dixitpatel.dchatdemo.feature_chat.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.dixitpatel.dchatdemo.feature_chat.presentation.navigation.AppNavigation
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.DChatDemoTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * The main and only activity of the application.
 *
 * This activity serves as the entry point for the user interface. It is annotated
 * with `@AndroidEntryPoint` to enable Hilt for dependency injection. The UI is built
 * entirely with Jetpack Compose.
 *
 * In `onCreate`, it sets up the edge-to-edge display and then sets the content
 * to the [AppEntryPoint] composable, which initializes the app's theme and navigation.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            AppEntryPoint()
        }
    }
}

@Composable
private fun AppEntryPoint() {
    DChatDemoTheme {
        AppNavigation()
    }
}
