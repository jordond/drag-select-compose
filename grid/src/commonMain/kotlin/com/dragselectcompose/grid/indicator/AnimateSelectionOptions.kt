package com.dragselectcompose.grid.indicator

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Options for animating the padding and shape of the selected item.
 *
 * Create an instance by calling [AnimateSelectionDefaults.options].
 *
 * @param[padding] The padding to apply to the selected item.
 * @param[cornerRadius] The corner radius to apply to the selected item.
 */
public class AnimateSelectionOptions internal constructor(
    public val padding: Dp,
    public val cornerRadius: Dp,
)

/**
 * Default values for [AnimateSelectionOptions].
 */
public object AnimateSelectionDefaults {

    private val Padding: Dp = 10.dp
    private val CornerRadius: Dp = 16.dp

    /**
     * Default instance of [AnimateSelectionOptions].
     */
    public val Default: AnimateSelectionOptions = AnimateSelectionOptions(Padding, CornerRadius)

    /**
     * Options for animating the padding and shape of the selected item.
     *
     * @param[padding] The padding to apply to the selected item.
     * @param[cornerRadius] The corner radius to apply to the selected item.
     */
    public fun options(
        padding: Dp = Default.padding,
        cornerRadius: Dp = Default.cornerRadius,
    ): AnimateSelectionOptions = AnimateSelectionOptions(padding, cornerRadius)
}