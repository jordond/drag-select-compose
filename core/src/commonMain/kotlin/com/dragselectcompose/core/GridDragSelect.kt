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
 * @param[key] a factory of stable and unique keys representing the item. Using the
 * same key for multiple items in the grid is not allowed. Type of the key should be saveable
 * via Bundle on Android. If null is passed the position in the grid will represent the key.
 * When you specify the key the scroll position will be maintained based on the key, which
 * means if you add/remove items before the current visible item the item with the given key
 * will be kept as the first visible one.
 * @return A [Modifier] that enables drag selection for a [LazyGridState].
 */
public fun <Item> Modifier.gridDragSelect(
    items: List<Item>,
    state: DragSelectState<Item>,
    enableAutoScroll: Boolean = true,
    autoScrollThreshold: Float? = null,
    enableHaptics: Boolean = true,
    hapticFeedback: HapticFeedback? = null,
    key: (Item) -> Any = { it as Any },
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

    val isSelected: (Item) -> Boolean = { item ->
        val itemKey = key(item)
        state.selected.find { key(it) == itemKey } != null
    }

    pointerInput(Unit) {
        detectDragGesturesAfterLongPress(
            onDragStart = { offset ->
                state.gridState.itemIndexAtPosition(offset)?.let { startIndex ->
                    val item = items.getOrNull(startIndex)
                    if (item != null && state.selected.contains(item).not()) {
                        haptics?.performHapticFeedback(HapticFeedbackType.LongPress)
                        state.startDrag(item, startIndex)
                    }
                }
            },
            onDragCancel = state::stopDrag,
            onDragEnd = state::stopDrag,
            onDrag = { change, _ ->
                state.whenDragging { dragState ->
                    autoScrollSpeed.value = gridState.calculateScrollSpeed(change, scrollThreshold)

                    val itemPosition = gridState.getItemPosition(change.position)
                        ?: return@whenDragging

                    val newSelection = items.getSelectedItems(itemPosition, dragState, isSelected)
                    updateDrag(current = itemPosition)
                    updateSelected(newSelection)
                }
            },
        )
    }
}

/**
 * Calculates the auto-scroll speed based on the current drag position.
 *
 * @param[change] The [PointerInputChange] for the current drag position.
 * @param[scrollThreshold] The distance from the edge of the grid to start auto-scrolling.
 * @return The auto-scroll speed.
 */
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

/**
 * Gets the index of the item that was hit by the drag.
 *
 * @param[hitPoint] The point where the drag hit the grid.
 * @return The index of the item that was hit by the drag.
 */
private fun LazyGridState.itemIndexAtPosition(hitPoint: Offset): Int? {
    val found = layoutInfo.visibleItemsInfo.find { itemInfo ->
        itemInfo.size.toIntRect().contains(hitPoint.round() - itemInfo.offset)
    }

    return found?.index
}

/**
 * Gets the index of the item that was hit by the drag.
 *
 * @param[hitPoint] The point where the drag hit the grid.
 * @return The index of the item that was hit by the drag, or the index of the last item if the
 * drag has gone past the last item.
 */
private fun LazyGridState.getItemPosition(hitPoint: Offset): Int? {
    return itemIndexAtPosition(hitPoint)
        ?: if (isPastLastItem(hitPoint)) layoutInfo.totalItemsCount - 1 else null
}

/**
 * Determines if the drag has gone past the last item in the list.
 *
 * @param[hitPoint] The point where the drag hit the grid.
 * @return True if the drag has gone past the last item in the list, false otherwise.
 */
private fun LazyGridState.isPastLastItem(hitPoint: Offset): Boolean {
    // Get the last item in the list
    val lastItem = layoutInfo.visibleItemsInfo.lastOrNull()
        ?.takeIf { it.index == layoutInfo.totalItemsCount - 1 }
        ?: return false

    // Determine if we have dragged past the last item in the list
    return hitPoint.y > lastItem.offset.y
}

/**
 * Gets a list of items that are selected based on the current drag state.
 *
 * @receiver The list of items in the grid.
 * @param[itemPosition] The position of the item that was hit by the drag.
 * @param[dragState] The current drag state.
 * @param[isSelected] A function to determine if an item is selected.
 * @return A list of items that are selected based on the current drag state.
 */
private fun <Item> List<Item>.getSelectedItems(
    itemPosition: Int,
    dragState: DragState,
    isSelected: (Item) -> Boolean,
): List<Item> {
    val (initial, current) = dragState
    return filterIndexed { index, item ->
        // Determine if the item is within the drag range
        val withinRange = index in initial..itemPosition || index in itemPosition..initial

        // Determine if the item was previously selected and is still within the drag range
        val selected = isSelected(item)
        val previouslySelectedInRange =
            selected && index !in initial..current && index !in current..initial

        withinRange || previouslySelectedInRange
    }
}
