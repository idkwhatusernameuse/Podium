package dev.idkwuu.allesandroid.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.load.engine.Resource
import dev.idkwuu.allesandroid.R
import dev.idkwuu.allesandroid.util.Theme

private val primaryColor = Color(0xFF3D90CD)
val disabledColor = Color(0xFF676767)

/// Neutral vote
val neutralColor = Color(0xFF424242)

var cardColor = Color.White

private val DarkColorPalette = darkColors(
    primary = primaryColor,
    primaryVariant = primaryColor,
    secondary = primaryColor,
    background = Color.Black,
    surface = Color.Black
)

private val LightColorPalette = lightColors(
    primary = primaryColor,
    primaryVariant = primaryColor,
    secondary = primaryColor,
    background = Color(0xFFF5F5F5),
    surface = Color(0xFFF5F5F5)
)

val t = Typography()
val typography = Typography(
    h6 = t.h6.copy(
        fontFamily = fontFamily(fonts = listOf(
            ResourceFont(resId = R.font.inter)
        )),
        fontWeight = FontWeight.SemiBold
    ),
    body1 = TextStyle(
        fontFamily = fontFamily(fonts = listOf(
            ResourceFont(resId = R.font.inter)
        )),
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
    ),
    body2  = TextStyle(
        fontFamily = fontFamily(fonts = listOf(
            ResourceFont(resId = R.font.inter)
        )),
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    )
)

val shapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(0.dp)
)

@Composable // WHY T
fun darkTheme(): Boolean {
    return when (Theme.get()) {
        1 -> false
        2 -> true
        else -> isSystemInDarkTheme()
    }
}

@Composable
fun AppTheme(darkTheme: Boolean = darkTheme(), content: @Composable() () -> Unit) {
    if (darkTheme) {
        cardColor = Color(0xFF212121)
    }

    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = typography,
        shapes = shapes,
        content = content
    )
}