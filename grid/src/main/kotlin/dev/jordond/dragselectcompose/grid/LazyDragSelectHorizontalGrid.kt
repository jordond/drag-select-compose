package dev.jordond.dragselectcompose.grid

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.unit.dp
import dev.jordond.dragselectcompose.DragSelectState
import dev.jordond.dragselectcompose.gridDragSelect
import dev.jordond.dragselectcompose.rememberDragSelectState

@Composable
public fun <Item> LazyDragSelectHorizontalGrid(
    rows: GridCells,
    items: List<Item>,
    modifier: Modifier = Modifier,
    state: DragSelectState<Item> = rememberDragSelectState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    horizontalArrangement: Arrangement.Horizontal =
        if (!reverseLayout) Arrangement.Start else Arrangement.End,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    userScrollEnabled: Boolean = true,
    enableAutoScroll: Boolean = true,
    autoScrollThreshold: Float? = null,
    enableHaptics: Boolean = true,
    hapticFeedback: HapticFeedback? = null,
    content: LazyDragSelectGridScope<Item>.() -> Unit,
) {
    val dragSelectModifier = modifier.gridDragSelect(
        items = items,
        state = state,
        enableAutoScroll = userScrollEnabled && enableAutoScroll,
        autoScrollThreshold = autoScrollThreshold,
        enableHaptics = enableHaptics,
        hapticFeedback = hapticFeedback,
    )

    LazyHorizontalGrid(
        rows = rows,
        modifier = dragSelectModifier,
        state = state.lazyGridState,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement,
        flingBehavior = flingBehavior,
        userScrollEnabled = userScrollEnabled,
        content = {
            val scope = DefaultLazyDragSelectGridScope(
                gridScope = this,
                items = items,
                stateProvider = { state },
            )

            content(scope)
        },
    )
}