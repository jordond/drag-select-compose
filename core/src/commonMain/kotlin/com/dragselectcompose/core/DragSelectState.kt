package com.dragselectcompose.core

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import dev.drewhamilton.poko.Poko

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
): DragSelectState<Item> = rememberDragSelectState({ it as Any }, lazyGridState, initialSelection)

/**
 * Creates a [DragSelectState] that is remembered across compositions.
 *
 * Changes to the provided initial values will **not** result in the state being recreated or
 * changed in any way if it has already been created.
 *
 * @param[Item] The type of the items in the list.
 * @param[lazyGridState] The [LazyGridState] that will be used to control the items in the grid.
 * @param[initialSelection] The initial selection of items.
 * @param[compareSelector] A factory for selecting a property of [Item] to compare.
 * @return A [DragSelectState] that can be used to control the selection.
 */
@Composable
public fun <Item> rememberDragSelectState(
    compareSelector: (Item) -> Any = { it as Any },
    lazyGridState: LazyGridState = rememberLazyGridState(),
    initialSelection: List<Item> = emptyList(),
): DragSelectState<Item> {
    val dragState = rememberSaveable(saver = DragState.Saver) { DragState.create() }

    return remember(lazyGridState) {
        DragSelectState(
            initialSelection = initialSelection,
            gridState = lazyGridState,
            compareSelector = compareSelector,
            dragState = dragState,
        )
    }
}

/**
 * A state object that can be hoisted to control and observe selected items.
 *
 * In most cases, this will be created via [rememberDragSelectState].
 *
 * @param[Item] The type of the items in the list.
 * @param[initialSelection] The initial selection of items.
 * @param[gridState] The [LazyGridState] that will be used to control the items in the grid.
 * @param[dragState] The current drag state.
 * @param[compareSelector] A factory for selecting a property of [Item] to compare.
 */
@Suppress("MemberVisibilityCanBePrivate")
@Poko
@Stable
public class DragSelectState<Item>(
    initialSelection: List<Item>,
    public val gridState: LazyGridState,
    internal val compareSelector: (Item) -> Any,
    internal var dragState: DragState,
) {

    /**
     * A state object that can be hoisted to control and observe selected items.
     *
     * In most cases, this will be created via [rememberDragSelectState].
     *
     * @param[Item] The type of the items in the list.
     * @param[gridState] The [LazyGridState] that will be used to control the items in the grid.
     * @param[initialIndex] The initial index of the item that was long pressed.
     * @param[initialSelection] The initial selection of items.
     */
    public constructor(
        gridState: LazyGridState,
        initialIndex: Int,
        initialSelection: List<Item>,
    ) : this(initialSelection, gridState, { it as Any }, DragState.create(initial = initialIndex))

    /**
     * The state containing the selected items.
     */
    private var selectedState: List<Item> by mutableStateOf(initialSelection)

    /**
     * The currently selected items.
     */
    @Stable
    public val selected: List<Item>
        get() = selectedState

    private var manualSelectionMode: Boolean by mutableStateOf(false)

    /**
     * Whether or not the grid is in selection mode.
     */
    @Stable
    public val inSelectionMode: Boolean
        get() = manualSelectionMode || selectedState.isNotEmpty()

    internal val autoScrollSpeed = mutableStateOf(0f)

    /**
     * Will only invoke [block] if the initial index is not [DragState.None].
     * Meaning we are in selection mode.
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
        dragState = DragState(initial = index, current = index)
        addSelected(item)
    }

    /**
     * Enables selection mode with no items selected.
     */
    public fun enableSelectionMode() {
        if (selectedState.isEmpty()) {
            manualSelectionMode = true
        }
    }

    /**
     * Disables selection mode and clear selected items.
     */
    public fun disableSelectionMode() {
        selectedState = emptyList()
        manualSelectionMode = false
    }

    /**
     * Toggle the selection mode.
     */
    public fun toggleSelectionMode() {
        if (inSelectionMode) disableSelectionMode()
        else enableSelectionMode()
    }

    /**
     * Whether or not the provided item is selected.
     *
     * @param[item] The item to check.
     * @return Whether or not the item is selected.
     */
    public fun isSelected(item: Item): Boolean = selectedState
        .find { compareSelector(it) == compareSelector(item) } != null

    /**
     * Updates the selected items.
     *
     * @param[selected] The new selected items.
     */
    public fun updateSelected(selected: List<Item>) {
        selectedState = selected

        if (selectedState.isEmpty() && manualSelectionMode) {
            manualSelectionMode = false
        }
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
    public fun removeSelected(item: Item) {
        selectedState -= item

        if (selectedState.isEmpty() && manualSelectionMode) {
            manualSelectionMode = false
        }
    }

    /**
     * Clears the selected items.
     */
    @Deprecated(
        message = "Use disableSelectionMode() instead. Will be removed in future version.",
        replaceWith = ReplaceWith("disableSelectionMode()"),
        level = DeprecationLevel.WARNING,
    )
    public fun clear() {
        disableSelectionMode()
    }

    /**
     * Resets the drag state.
     */
    internal fun stopDrag() {
        dragState = dragState.copy(initial = DragState.None)
        autoScrollSpeed.value = 0f
    }
}
