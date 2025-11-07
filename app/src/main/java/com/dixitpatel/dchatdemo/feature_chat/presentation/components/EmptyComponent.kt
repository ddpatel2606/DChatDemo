package com.dixitpatel.dchatdemo.feature_chat.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dixitpatel.dchatdemo.R
import com.dixitpatel.dchatdemo.feature_chat.presentation.theme.DChatDemoTheme
import kotlin.text.isNotEmpty

@Preview
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
                modifier = Modifier.padding(horizontal = 16.dp).align(Alignment.Center),
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
