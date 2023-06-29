package com.dragselectcompose.grid

import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.unit.dp
import com.dragselectcompose.core.DragSelectState
import com.dragselectcompose.core.gridDragSelect
import com.dragselectcompose.core.rememberDragSelectState

/**
 * A wrapper for [LazyHorizontalGrid] that provides drag selection functionality.
 *
 * You can use [LazyDragSelectGridItemScope.SelectableItem] to wrap your item composable to
 * make it selectable via a long press, or when selected via the drag-select.
 *
 * Example usage:
 *
 * ```
 * @Composable
 * fun MyGrid() {
 *     val items: List<Int> = List(100) { it }
 *     val dragSelectState = rememberDragSelectState<Int>()
 *
 *     LazyDragSelectHorizontalGrid(
 *         rows = GridCells.Adaptive(minSize = 128.dp),
 *         items = items,
 *         state = dragSelectState,
 *         horizontalArrangement = Arrangement.spacedBy(3.dp),
 *         verticalArrangement = Arrangement.spacedBy(3.dp),
 *     ) {
 *         items { item ->
 *             // SelectableItem is available on the grid scope
 *             SelectableItem(item = item) { selected ->
 *                  // your item composable
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * @see LazyVerticalGrid
 * @see LazyDragSelectGridItemScope.SelectableItem
 * @param[Item] The type of item in the grid.
 * @param[rows] a class describing how cells form rows, see [GridCells] doc for more information.
 * @param[modifier] the modifier to apply to this layout.
 * @param[state] the state object to be used to control or observe the list's state.
 * @param[contentPadding] specify a padding around the whole content.
 * @param[reverseLayout] reverse the direction of scrolling and layout. When `true`, items will be
 * laid out in the reverse order and `LazyGridState.firstVisibleItemIndex == 0` means that grid is
 * scrolled to the bottom. Note that [reverseLayout] does not change the behavior of [verticalArrangement].
 * @param[verticalArrangement] The vertical arrangement of the layout's children.
 * @param[horizontalArrangement] The horizontal arrangement of the layout's children.
 * @param[flingBehavior] logic describing fling behavior.
 * @param[userScrollEnabled] whether the scrolling via the user gestures or accessibility actions
 * is allowed. You can still scroll programmatically using the state even when it is disabled.
 * @param[enableAutoScroll] whether to enable auto scroll when dragging outside of the grid.
 * @param[autoScrollThreshold] the threshold in dp from the edge of the grid to start auto scroll.
 * @param[enableHaptics] whether to enable haptic feedback when dragging outside of the grid.
 * @param[hapticFeedback] the haptic feedback to be played when dragging outside of the grid.
 * @param[content] the LazyGridScope which describes the content.
 */
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
        state = state.gridState,
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