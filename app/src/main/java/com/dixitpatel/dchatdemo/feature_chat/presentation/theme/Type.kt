package com.dixitpatel.dchatdemo.feature_chat.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dixitpatel.dchatdemo.R

private val CobaneFont = FontFamily(
    Font(R.font.core_cobane_regular, weight = FontWeight.Normal),
    Font(R.font.core_cobane_medium, weight = FontWeight.W500),
    Font(R.font.core_cobane_bold, weight = FontWeight.Bold),
    Font(R.font.core_cobane_light, weight = FontWeight.Light),
    Font(R.font.core_cobane_semibold, weight = FontWeight.SemiBold),
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = CobaneFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ), titleLarge = TextStyle(
        fontFamily = CobaneFont,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ), labelSmall = TextStyle(
        fontFamily = CobaneFont,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)