package com.dixitpatel.dchatdemo.feature_chat.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import com.dixitpatel.dchatdemo.feature_chat.presentation.navigation.AppNavigation
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.DChatDemoTheme
import dagger.hilt.android.AndroidEntryPoint

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
