package com.lininglink.mechanicalpencils.ui.components

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.lininglink.mechanicalpencils.R

enum class SymbolWeight {
    Regular, Medium, Bold
}

private val materialSymbolsFamily = FontFamily(
    Font(R.font.material_symbols_outlined_regular, FontWeight.Normal),
    Font(R.font.material_symbols_outlined_medium, FontWeight.Medium),
    Font(R.font.material_symbols_outlined_bold, FontWeight.Bold)
)

@Composable
fun MaterialSymbol(
    name: String,
    modifier: Modifier = Modifier,
    size: TextUnit = 24.sp,
    weight: SymbolWeight = SymbolWeight.Regular,
    tint: Color = LocalContentColor.current
) {
    Text(
        text = name,
        modifier = modifier,
        style = TextStyle(
            fontFamily = materialSymbolsFamily,
            fontWeight = when (weight) {
                SymbolWeight.Regular -> FontWeight.Normal
                SymbolWeight.Medium -> FontWeight.Medium
                SymbolWeight.Bold -> FontWeight.Bold
            },
            fontSize = size
        ),
        color = tint
    )
}
