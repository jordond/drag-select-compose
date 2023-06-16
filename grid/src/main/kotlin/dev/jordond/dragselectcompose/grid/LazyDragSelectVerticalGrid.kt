package dev.jordond.dragselectcompose.grid

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.unit.dp
import dev.jordond.dragselectcompose.DragSelectState
import dev.jordond.dragselectcompose.gridDragSelect
import dev.jordond.dragselectcompose.rememberDragSelectState

@Composable
public fun <Item> LazyDragSelectVerticalGrid(
    columns: GridCells,
    items: List<Item>,
    modifier: Modifier = Modifier,
    state: DragSelectState<Item> = rememberDragSelectState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    reverseLayout: Boolean = false,
    verticalArrangement: Arrangement.Vertical =
        if (!reverseLayout) Arrangement.Top else Arrangement.Bottom,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
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

    LazyVerticalGrid(
        columns = columns,
        modifier = dragSelectModifier,
        state = state.lazyGridState,
        contentPadding = contentPadding,
        reverseLayout = reverseLayout,
        verticalArrangement = verticalArrangement,
        horizontalArrangement = horizontalArrangement,
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