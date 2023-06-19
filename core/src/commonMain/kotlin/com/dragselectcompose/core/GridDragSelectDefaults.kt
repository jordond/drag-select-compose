package com.dragselectcompose.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

/**
 * Default values for [Modifier.gridDragSelect].
 */
public object GridDragSelectDefaults {

    /**
     * Default value for [HapticFeedback].
     */
    public val hapticsFeedback: HapticFeedback
        @Composable get() = LocalHapticFeedback.current

    /**
     * Default value to determine when to start auto-scrolling.
     */
    public val autoScrollThreshold: Float
        @Composable get() = with(LocalDensity.current) { DEFAULT_THRESHOLD_DP.dp.toPx() }

    private const val DEFAULT_THRESHOLD_DP = 40
}
