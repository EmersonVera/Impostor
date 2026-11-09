package com.ufps.impostor.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ufps.impostor.R

// Plus Jakarta Sans es una fuente variable: cada peso se pide al mismo archivo
@OptIn(ExperimentalTextApi::class)
private fun jakarta(weight: Int) = Font(
    resId = R.font.plus_jakarta_sans,
    weight = FontWeight(weight),
    variationSettings = FontVariation.Settings(FontVariation.weight(weight))
)

val PlusJakartaSans = FontFamily(
    jakarta(400),
    jakarta(500),
    jakarta(600),
    jakarta(700),
    jakarta(800)
)

private fun style(size: Int, weight: FontWeight, lineHeight: Int) = TextStyle(
    fontFamily = PlusJakartaSans,
    fontWeight = weight,
    fontSize = size.sp,
    lineHeight = lineHeight.sp
)

val Typography = Typography(
    headlineLarge = style(32, FontWeight.ExtraBold, 38),
    headlineMedium = style(26, FontWeight.ExtraBold, 32),
    headlineSmall = style(22, FontWeight.ExtraBold, 28),
    titleLarge = style(19, FontWeight.ExtraBold, 25),
    titleMedium = style(16, FontWeight.Bold, 22),
    titleSmall = style(14, FontWeight.Bold, 20),
    bodyLarge = style(16, FontWeight.Normal, 23),
    bodyMedium = style(14, FontWeight.Normal, 20),
    bodySmall = style(12, FontWeight.Medium, 17),
    labelLarge = style(13, FontWeight.Bold, 18),
    labelMedium = style(11, FontWeight.Bold, 15),
    labelSmall = style(10, FontWeight.Bold, 14)
)
