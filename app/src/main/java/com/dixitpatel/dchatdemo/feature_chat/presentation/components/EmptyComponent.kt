package com.dixitpatel.dchatdemo.feature_chat.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.DChatDemoTheme
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.dimens

/**
 * A composable function that displays a centered text message when a given string is not empty.
 * It's typically used to show "empty state" messages to the user.
 *
 * @param modifier The modifier to be applied to the layout.
 * @param data The string data to be displayed. If this string is empty, nothing will be shown.
 */
@Composable
fun EmptyView(
    modifier: Modifier = Modifier,
    data: String = ""
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
    ) {
        if (data.isNotEmpty()) {
            Text(
                text = data,
                lineHeight = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = MaterialTheme.dimens.m).align(Alignment.Center),
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    DChatDemoTheme {
        EmptyView(modifier = Modifier,"Empty Data")
    }
}
