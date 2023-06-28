package com.dragselectcompose.core

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.toIntRect
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

/**
 * A [Modifier] that enables drag selection for a [LazyGridState].
 *
 * Example usage:
 *
 * ```
 * val items: List<Int> = (0..100).map { it }
 *
 * LazyVerticalGrid(
 *     state = dragSelectState.lazyGridState,
 *     columns = GridCells.Adaptive(minSize = 128.dp),
 *     verticalArrangement = Arrangement.spacedBy(3.dp),
 *     horizontalArrangement = Arrangement.spacedBy(3.dp),
 *     modifier = modifier.gridDragSelect(
 *         items = items,
 *         state = dragSelectState,
 *     ),
 * ) {
 *    // Build your gird
 * }
 * ```
 *
 * @param[Item] The type of item in the grid.
 * @param[items] The list of items in the grid.
 * @param[state] The [DragSelectState] for maintaining state
 * @param[enableAutoScroll] Whether to enable auto-scrolling when dragging near the edge of the grid.
 * @param[autoScrollThreshold] The distance from the edge of the grid to start auto-scrolling.
 * @param[enableHaptics] Whether to enable haptic feedback when dragging.
 * @param[hapticFeedback] The [HapticFeedback] to use, defaults to [GridDragSelectDefaults.hapticsFeedback].
 * @return A [Modifier] that enables drag selection for a [LazyGridState].
 */
public fun <Item> Modifier.gridDragSelect(
    items: List<Item>,
    state: DragSelectState<Item>,
    enableAutoScroll: Boolean = true,
    autoScrollThreshold: Float? = null,
    enableHaptics: Boolean = true,
    hapticFeedback: HapticFeedback? = null,
): Modifier = composed {
    val scrollThreshold: Float = autoScrollThreshold ?: GridDragSelectDefaults.autoScrollThreshold
    if (enableAutoScroll) {
        LaunchedEffect(state.autoScrollSpeed.value) {
            if (state.autoScrollSpeed.value == 0f) return@LaunchedEffect

            while (isActive) {
                state.gridState.scrollBy(state.autoScrollSpeed.value)
                delay(10)
            }
        }
    }

    val haptics: HapticFeedback? =
        if (!enableHaptics) null
        else hapticFeedback ?: GridDragSelectDefaults.hapticsFeedback

    pointerInput(Unit) {
        detectDragGesturesAfterLongPress(
            onDragStart = { offset ->
                state.gridState.itemIndexAtPosition(offset)?.let { startIndex ->
                    val item = items.getOrNull(startIndex)
                    if (item != null && state.selected.contains(item).not()) {
                        haptics?.performHapticFeedback(HapticFeedbackType.LongPress)
                        state.initialIndex = startIndex
                        state.addSelected(item)
                    }
                }
            },
            onDragCancel = state::resetDrag,
            onDragEnd = state::resetDrag,
            onDrag = { change, _ ->
                state.withInitialIndex { initial ->
                    autoScrollSpeed.value = gridState.calculateScrollSpeed(change, scrollThreshold)

                    val newSelected =
                        gridState.getSelectedByPosition(items, initial, change)
                            ?: gridState.getOverscrollItems(items, initial, change)

                    if (newSelected != null) {
                        updateSelected(newSelected)
                    }
                }
            },
        )
    }
}

private fun LazyGridState.calculateScrollSpeed(
    change: PointerInputChange,
    scrollThreshold: Float,
): Float {
    val distanceFromTop: Float = change.position.y
    val distanceFromBottom: Float = layoutInfo.viewportSize.height - distanceFromTop

    return when {
        distanceFromBottom < scrollThreshold -> scrollThreshold - distanceFromBottom
        distanceFromTop < scrollThreshold -> -(scrollThreshold - distanceFromTop)
        else -> 0f
    }
}

private fun LazyGridState.itemIndexAtPosition(hitPoint: Offset): Int? {
    val found = layoutInfo.visibleItemsInfo.find { itemInfo ->
        itemInfo.size.toIntRect().contains(hitPoint.round() - itemInfo.offset)
    }

    return found?.index
}

private fun <Item> LazyGridState.getSelectedByPosition(
    items: List<Item>,
    initialIndex: Int,
    change: PointerInputChange,
): List<Item>? {
    val itemAtPosition = itemIndexAtPosition(change.position) ?: return null
    return items.filterIndexed { index, _ ->
        index in initialIndex..itemAtPosition || index in itemAtPosition..initialIndex
    }
}

/**
 * Get the items that are overscrolled when dragging.
 *
 * If the user has dragged past the last item in the list, this will return all items after the
 * initial index.
 */
private fun <Item> LazyGridState.getOverscrollItems(
    items: List<Item>,
    initialIndex: Int,
    change: PointerInputChange,
): List<Item>? {
    // Get the last item in the list
    val lastItem = layoutInfo.visibleItemsInfo.lastOrNull()
        ?.takeIf { it.index == layoutInfo.totalItemsCount - 1 }
        ?: return null

    // Determine if we have dragged past the last item in the list
    return  if (change.position.y > lastItem.offset.y) {
        // If we have, return all items after the initial index
        items.filterIndexed { index, _ -> index >= initialIndex }
    } else null
}
