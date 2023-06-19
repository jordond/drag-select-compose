package com.dragselectcompose.grid.indicator

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp

/**
 * Options for the indicator icon.
 *
 * @param[icon] Icon [ImageVector] to display.
 * @param[tint] Tint to apply to the icon.
 * @param[contentDescription] Content description to apply to the icon.
 * @param[padding] Padding to apply to the icon.
 * @param[backgroundColor] Background color to apply to the icon.
 * @param[border] Border to apply to the icon.
 * @param[shape] Shape to clip the icon to.
 */
public data class IndicatorIconOptions(
    val icon: ImageVector,
    val tint: Color,
    val contentDescription: String?,
    val padding: Dp,
    val backgroundColor: Color?,
    val border: Dp?,
    val shape: Shape?,
)