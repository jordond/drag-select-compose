package com.dragselectcompose.core

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.dragselectcompose.core.DragState.Companion.None

/**
 * Creates a [DragSelectState] that is remembered across compositions.
 *
 * Changes to the provided initial values will **not** result in the state being recreated or
 * changed in any way if it has already been created.
 *
 * @param[Item] The type of the items in the list.
 * @param[lazyGridState] The [LazyGridState] that will be used to control the items in the grid.
 * @param[initialSelection] The initial selection of items.
 * @return A [DragSelectState] that can be used to control the selection.
 */
@Composable
public fun <Item> rememberDragSelectState(
    lazyGridState: LazyGridState = rememberLazyGridState(),
    initialSelection: List<Item> = emptyList(),
): DragSelectState<Item> {
    val indexes by rememberSaveable { mutableStateOf(None to None) }
    return remember(lazyGridState) {
        DragSelectState(lazyGridState, indexes.first, initialSelection, indexes.second)
    }
}

/**
 * A state object that can be hoisted to control and observe selected items.
 *
 * In most cases, this will be created via [rememberDragSelectState].
 *
 * @param[Item] The type of the items in the list.
 * @param[gridState] The [LazyGridState] that will be used to control the items in the grid.
 * @param[initialIndex] The initial index of the item that was long pressed.
 * @param[initialSelection] The initial selection of items.
 * @param[currentIndex] The current index of the item that is being dragged over.
 */
public class DragSelectState<Item>(
    public val gridState: LazyGridState,
    initialIndex: Int,
    initialSelection: List<Item>,
    currentIndex: Int = initialIndex,
) {

    public constructor(
        gridState: LazyGridState,
        initialSelection: List<Item>,
    ) : this(gridState, None, initialSelection, None)

    private var dragState: DragState = DragState(initialIndex, currentIndex)

    /**
     * The state containing the selected items.
     */
    private var selectedState: List<Item> by mutableStateOf(initialSelection)

    /**
     * The currently selected items.
     */
    public val selected: List<Item>
        get() = selectedState

    /**
     * Whether or not the grid is in selection mode.
     */
    public val inSelectionMode: Boolean
        get() = selectedState.isNotEmpty()

    internal val autoScrollSpeed = mutableStateOf(0f)

    /**
     * Will only invoke [block] if the initial index is not [None]. Meaning we are in selection mode.
     */
    internal fun whenDragging(
        block: DragSelectState<Item>.(dragState: DragState) -> Unit,
    ) {
        if (dragState.isDragging) {
            block(this, dragState)
        }
    }

    internal fun updateDrag(current: Int) {
        dragState = dragState.copy(current = current)
    }

    internal fun startDrag(item: Item, index: Int) {
        dragState = DragState(index, index)
        addSelected(item)
    }

    /**
     * Whether or not the provided item is selected.
     *
     * @param[item] The item to check.
     * @return Whether or not the item is selected.
     */
    public fun isSelected(item: Item): Boolean = selectedState.contains(item)

    /**
     * Updates the selected items.
     *
     * @param[selected] The new selected items.
     */
    public fun updateSelected(selected: List<Item>) {
        selectedState = selected
    }

    /**
     * Adds the provided item to the selected items.
     *
     * @param[item] The item to add.
     */
    public fun addSelected(item: Item) {
        selectedState += item
    }

    /**
     * Removes the provided item from the selected items.
     *
     * @param[item] The item to remove.
     */
    public fun removeSelected(photo: Item) {
        selectedState -= photo
    }

    /**
     * Clears the selected items.
     */
    public fun clear() {
        selectedState = emptyList()
    }

    /**
     * Resets the drag state.
     */
    internal fun stopDrag() {
        dragState = dragState.copy(initial = DragState.None)
        autoScrollSpeed.value = 0f
    }
}
