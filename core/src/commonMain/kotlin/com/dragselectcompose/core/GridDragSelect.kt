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

    val isSelected: (Item) -> Boolean = { item ->
        state.selected.contains(item)
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

private fun LazyGridState.getItemPosition(offset: Offset): Int? {
    return itemIndexAtPosition(offset)
        ?: if (isPastLastItem(offset)) layoutInfo.totalItemsCount - 1 else null
}

private fun LazyGridState.isPastLastItem(offset: Offset): Boolean {
    // Get the last item in the list
    val lastItem = layoutInfo.visibleItemsInfo.lastOrNull()
        ?.takeIf { it.index == layoutInfo.totalItemsCount - 1 }
        ?: return false

    // Determine if we have dragged past the last item in the list
    return offset.y > lastItem.offset.y
}

private fun <Item> List<Item>.getSelectedItems(
    itemPosition: Int,
    dragState: DragState,
    isSelected: (Item) -> Boolean,
): List<Item> {
    val (initial, current) = dragState

    return filterIndexed { index, item ->

        val withinRange = index in initial..itemPosition || index in itemPosition..initial

        val selected = isSelected(item)
        val previouslySelectedInRange =
            selected && index !in initial..current && index !in current..initial

        withinRange || previouslySelectedInRange
    }
}
