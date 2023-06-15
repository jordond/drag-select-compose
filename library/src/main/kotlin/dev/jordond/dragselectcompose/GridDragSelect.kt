package dev.jordond.dragselectcompose

import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.round
import androidx.compose.ui.unit.toIntRect
import kotlinx.coroutines.isActive

public fun <Item> Modifier.gridDragSelect(
    lazyGridState: LazyGridState,
    items: List<Item>,
    selected: MutableState<List<Item>>,
    enableAutoScroll: Boolean = true,
    autoScrollThreshold: Float? = null,
    enableHaptics: Boolean = true,
    hapticFeedback: HapticFeedback? = null,
): Modifier = composed {
    val scrollThreshold: Float = autoScrollThreshold ?: GridDragSelectDefaults.autoScrollThreshold
    var autoScrollSpeed by remember { mutableStateOf(0f) }
    if (enableAutoScroll) {
        LaunchedEffect(autoScrollSpeed) {
            if (autoScrollSpeed == 0f) return@LaunchedEffect

            while (isActive) {
                lazyGridState.scrollBy(autoScrollSpeed)
            }
        }
    }

    val haptics: HapticFeedback? =
        if (!enableHaptics) null
        else hapticFeedback ?: GridDragSelectDefaults.hapticsFeedback

    var initialIndex: Int? = null
    var currentIndex: Int? = null
    val reset = {
        initialIndex = null
        autoScrollSpeed = 0f
    }

    pointerInput(Unit) {
        detectDragGesturesAfterLongPress(
            onDragStart = { offset ->
                lazyGridState.itemIndexAtPosition(offset)?.let { index ->
                    val item = items.getOrNull(index)
                    if (item != null && selected.value.contains(item).not()) {
                        haptics?.performHapticFeedback(HapticFeedbackType.LongPress)
                        initialIndex = index
                        currentIndex = index
                        selected.value += item
                    }
                }
            },
            onDragCancel = reset,
            onDragEnd = reset,
            onDrag = { change, _ ->
                val initial = initialIndex
                if (initial != null) {
                    autoScrollSpeed = lazyGridState.calculateNewScrollSpeed(change, scrollThreshold)

                    lazyGridState.itemIndexAtPosition(change.position)?.let { newIndex ->
                        if (currentIndex == null || initialIndex == null) return@let

//                        val initialIndex = items.indexOfFirst { keys(it) == initialIndex }
                        val newSelectedKeys = items.filterIndexed { index, _ ->
                            index in initialIndex!!..newIndex ||
                                index in newIndex..initialIndex!!
                        }

                        selected.value = newSelectedKeys
                        currentIndex = newIndex
                    }
                }
            },
        )
    }
}

private fun LazyGridState.calculateNewScrollSpeed(
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