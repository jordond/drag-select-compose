package com.dragselectcompose.core

import androidx.compose.runtime.Stable
import androidx.compose.runtime.saveable.Saver

/**
 * Represents the current state of a drag gesture.
 *
 * @param[initial] The index of the item where the drag gesture started.
 * @param[current] The index of the item where the drag gesture is currently at.
 */
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as DragState

        if (initial != other.initial) return false
        return current == other.current
    }

    override fun hashCode(): Int {
        var result = initial
        result = 31 * result + current
        return result
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