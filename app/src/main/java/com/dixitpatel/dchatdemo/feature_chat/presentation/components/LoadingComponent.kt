package com.dixitpatel.dchatdemo.feature_chat.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.DChatDemoTheme
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.dimens

@Composable
fun LoadingComponent(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .testTag("LoadingComponent")
                .fillMaxSize()
                .padding(MaterialTheme.dimens.s)
                .background(MaterialTheme.colorScheme.background),
    ) {
        CircularProgressIndicator(
            modifier =
                Modifier
                    .align(Alignment.Center),
        )
    }
}

@Preview
@Composable
private fun Preview() {
    DChatDemoTheme {
        LoadingComponent()
    }
}
