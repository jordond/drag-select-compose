package com.dragselectcompose.core

import androidx.compose.runtime.saveable.Saver

/**
 * Represents the current state of a drag gesture.
 *
 * @param[initial] The index of the item where the drag gesture started.
 * @param[current] The index of the item where the drag gesture is currently at.
 */
public class DragState(
    internal var initial: Int,
    internal var current: Int,
) {

    internal val isDragging: Boolean
        get() = initial != None && current != None

    internal companion object {

        internal const val None = -1

        internal fun create(
            initial: Int = None,
            current: Int = None,
        ): DragState = DragState(initial, current)

        internal val Saver = Saver<DragState, Pair<Int, Int>>(
            save = { it.initial to it.current },
            restore = { (initial, current) -> create(initial = initial, current = current) }
        )
    }
}