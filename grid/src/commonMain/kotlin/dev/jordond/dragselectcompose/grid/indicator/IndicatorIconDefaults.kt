package dev.jordond.dragselectcompose.grid.indicator

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Default options for [IndicatorIcon].
 */
public object IndicatorIconDefaults {

    /**
     * Default shape for [IndicatorIcon].
     */
    public val IconShape: Shape
        get() = CircleShape

    /**
     * Default options for 'selected' [IndicatorIcon].
     *
     * @see IndicatorIconOptions
     * @see SelectedIcon
     */
    @Composable
    public fun selectedIconOptions(
        icon: ImageVector = Icons.Filled.CheckCircle,
        tint: Color = MaterialTheme.colorScheme.primary,
        contentDescription: String? = "Selected",
        padding: Dp = 4.dp,
        backgroundColor: Color = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp),
        border: Dp = 2.dp,
        shape: Shape = IconShape,
    ): IndicatorIconOptions = IndicatorIconOptions(
        icon = icon,
        tint = tint,
        contentDescription = contentDescription,
        padding = padding,
        backgroundColor = backgroundColor,
        border = border,
        shape = shape,
    )

    /**
     * Default options for 'unselected' [IndicatorIcon].
     *
     * @see IndicatorIconOptions
     * @see UnselectedIcon
     */
    @Composable
    public fun unselectedIconOptions(
        icon: ImageVector = Icons.Filled.RadioButtonUnchecked,
        tint: Color = Color.White.copy(alpha = 0.7f),
        contentDescription: String? = "Not Selected",
        padding: Dp = 4.dp,
    ): IndicatorIconOptions = IndicatorIconOptions(
        icon = icon,
        tint = tint,
        contentDescription = contentDescription,
        padding = padding,
        backgroundColor = null,
        border = null,
        shape = null,
    )
}