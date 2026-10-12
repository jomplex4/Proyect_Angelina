package com.digitalminds.angelina

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

/** Single source of truth for brand tokens. Roles assigned from the provided palette (see README). */
object Brand {
    val bg = Color(0xFF000000)
    val surface = Color(0xFF151517)
    val keyNum = Color(0xFF1F2023)
    val keyOp = Color(0xFF2B2C31)
    val keyFn = Color(0xFF151517)
    val line = Color(0xFF2A2B30)
    val accent = Color(0xFFDC0000)
    val accentHi = Color(0xFFFF2A1F)
    val text = Color(0xFFFFFFFF)
    val textDim = Color(0xFF9A9CA4)
    val error = Color(0xFFFF5C8A)
    val violet = Color(0xFFA78BFA)
    val blue = Color(0xFF5B9BFF)
    val orange = Color(0xFFFF9A4D)
    val pink = Color(0xFFFF6FAE)
    val series = listOf(accentHi, blue, violet, orange, pink)

    val sans = FontFamily(Font(R.font.space_grotesk_regular, FontWeight.Normal), Font(R.font.space_grotesk_bold, FontWeight.Bold))
    val mono = FontFamily.Monospace
}
