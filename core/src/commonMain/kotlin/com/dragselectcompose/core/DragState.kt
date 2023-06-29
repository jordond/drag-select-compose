package com.dragselectcompose.core

/**
 * Represents the current state of a drag gesture.
 *
 * @param[initial] The index of the item where the drag gesture started.
 * @param[current] The index of the item where the drag gesture is currently at.
 */
internal data class DragState(
    val initial: Int,
    val current: Int,
) {

    internal val isDragging: Boolean
        get() = initial != None && current != None

    internal companion object {

        internal const val None = -1
    }
}