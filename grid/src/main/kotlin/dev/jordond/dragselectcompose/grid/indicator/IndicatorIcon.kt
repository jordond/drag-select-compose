@file:Suppress("UnusedReceiverParameter")

package dev.jordond.dragselectcompose.grid.indicator

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import dev.jordond.dragselectcompose.grid.LazyDragSelectGridItemScope

/**
 * Indicator icon for the selected state.
 *
 * @param[modifier] Modifier to be applied to the icon.
 * @param[options] Options to customize the icon.
 */
@Composable
public fun LazyDragSelectGridItemScope<*>.SelectedIcon(
    modifier: Modifier = Modifier,
    options: IndicatorIconOptions = IndicatorIconDefaults.selectedIconOptions(),
): Unit = IndicatorIcon(options, modifier)

/**
 * Indicator icon for the unselected state.
 *
 * @param[modifier] Modifier to be applied to the icon.
 * @param[options] Options to customize the icon.
 */
@Composable
public fun LazyDragSelectGridItemScope<*>.UnselectedIcon(
    modifier: Modifier = Modifier,
    options: IndicatorIconOptions = IndicatorIconDefaults.unselectedIconOptions(),
): Unit = IndicatorIcon(options, modifier)

/**
 * Indicator overlay to display the selected or unselected state of an item.
 *
 * @param[modifier] Modifier to be applied to the icon.
 * @param[options] Options to customize the icon.
 */
@Composable
public fun LazyDragSelectGridItemScope<*>.IndicatorIcon(
    options: IndicatorIconOptions,
    modifier: Modifier = Modifier,
) {
    IndicatorIcon(
        icon = options.icon,
        tint = options.tint,
        contentDescription = options.contentDescription,
        padding = options.padding,
        backgroundColor = options.backgroundColor,
        border = options.border,
        shape = options.shape,
        modifier = modifier,
    )
}

/**
 * Indicator overlay to display the selected or unselected state of an item.
 *
 * @param[icon] Icon [ImageVector] to display.
 * @param[tint] Tint to apply to the icon.
 * @param[contentDescription] Content description to apply to the icon.
 * @param[padding] Padding to apply to the icon.
 * @param[backgroundColor] Background color to apply to the icon.
 * @param[border] Border to apply to the icon.
 * @param[shape] Shape to clip the icon to.
 * @param[modifier] Modifier to be applied to the icon.
 */
@Composable
public fun LazyDragSelectGridItemScope<*>.IndicatorIcon(
    icon: ImageVector,
    tint: Color,
    contentDescription: String?,
    padding: Dp,
    backgroundColor: Color?,
    border: Dp?,
    shape: Shape?,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = icon,
        tint = tint,
        contentDescription = contentDescription,
        modifier = modifier
            .padding(padding)
            .border(border, backgroundColor, shape)
            .clip(shape)
            .background(backgroundColor)
    )
}

private fun Modifier.border(width: Dp?, color: Color?, shape: Shape?): Modifier {
    return if (width == null || color == null || shape == null) this
    else then(Modifier.border(width, color, shape))
}

private fun Modifier.clip(shape: Shape?): Modifier {
    return if (shape == null) this
    else then(Modifier.clip(shape))
}

private fun Modifier.background(color: Color?): Modifier {
    return if (color == null) this
    else then(Modifier.background(color))
}
