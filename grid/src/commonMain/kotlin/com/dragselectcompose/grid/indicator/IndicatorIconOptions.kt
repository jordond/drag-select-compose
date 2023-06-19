package com.dragselectcompose.grid.indicator

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp

/**
 * Options for the indicator icon.
 *
 * Use [IndicatorIconDefaults.selectedIconOptions] or [IndicatorIconDefaults.unselectedIconOptions]
 * to create a default instance of this class.
 *
 * @param[icon] Icon [ImageVector] to display.
 * @param[tint] Tint to apply to the icon.
 * @param[contentDescription] Content description to apply to the icon.
 * @param[padding] Padding to apply to the icon.
 * @param[backgroundColor] Background color to apply to the icon.
 * @param[border] Border to apply to the icon.
 * @param[shape] Shape to clip the icon to.
 */
public class IndicatorIconOptions internal constructor(
    public val icon: ImageVector,
    public val tint: Color,
    public val contentDescription: String?,
    public val padding: Dp,
    public val backgroundColor: Color?,
    public val border: Dp?,
    public val shape: Shape?,
)