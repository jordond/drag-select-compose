package com.dragselectcompose.core

import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.Saver
import dev.drewhamilton.poko.Poko

/**
 * Represents the current state of a drag gesture.
 *
 * @param[initial] The index of the item where the drag gesture started.
 * @param[current] The index of the item where the drag gesture is currently at.
 */
@Poko
@Stable
public class DragState(
    internal val initial: Int,
    internal val current: Int,
) {

    internal val isDragging: Boolean
        get() = initial != None && current != None

    internal fun copy(initial: Int = this.initial, current: Int = this.current): DragState {
        return DragState(initial = initial, current = current)
    }

    internal companion object {

        internal const val None = -1

        internal fun create(
            initial: Int = None,
            current: Int = None,
        ): DragState = DragState(initial, current)

        internal val Saver = Saver<DragState, Pair<Int, Int>>(
            save = { it.initial to it.current },
            restore = { (initial, current) -> DragState(initial = initial, current = current) },
        )
    }
}