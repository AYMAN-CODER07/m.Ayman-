package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = CyanPrimary,
    onPrimary = OnCyanPrimary,
    primaryContainer = CyanContainer,
    onPrimaryContainer = OnCyanContainer,
    secondary = AmberSecondary,
    onSecondary = OnAmberSecondary,
    secondaryContainer = AmberContainer,
    onSecondaryContainer = OnAmberContainer,
    tertiary = VioletTertiary,
    onTertiary = OnVioletTertiary,
    tertiaryContainer = VioletContainer,
    onTertiaryContainer = OnVioletContainer,
    background = SiliconBg,
    onBackground = Color(0xFFE6EDF8),
    surface = SiliconSurface,
    onSurface = Color(0xFFE6EDF8),
    surfaceVariant = SiliconSurfaceVariant,
    onSurfaceVariant = Color(0xFFA3B3D1),
    outline = SiliconOutline,
    error = CoralError,
    onError = Color(0xFF3D0011)
)

private val LightColorScheme = lightColorScheme(
    primary = CobaltPrimary,
    onPrimary = OnCobaltPrimary,
    primaryContainer = CobaltContainer,
    onPrimaryContainer = OnCobaltContainer,
    secondary = CopperSecondary,
    onSecondary = OnCopperSecondary,
    secondaryContainer = CopperContainer,
    onSecondaryContainer = OnCopperContainer,
    tertiary = IndigoTertiary,
    onTertiary = OnIndigoTertiary,
    tertiaryContainer = IndigoContainer,
    onTertiaryContainer = OnIndigoContainer,
    background = BlueprintBg,
    onBackground = Color(0xFF0F172A),
    surface = BlueprintSurface,
    onSurface = Color(0xFF0F172A),
    surfaceVariant = BlueprintSurfaceVariant,
    onSurfaceVariant = Color(0xFF475569),
    outline = BlueprintOutline,
    error = Color(0xFFDC2626),
    onError = Color.White
)

val ArchShapes = Shapes(
    small = RoundedCornerShape(10.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun Arch3dCoaTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = ArchTypography,
        shapes = ArchShapes,
        content = content
    )
}
