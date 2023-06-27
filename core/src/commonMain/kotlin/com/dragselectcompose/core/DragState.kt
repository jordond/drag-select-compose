package com.dragselectcompose.core

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