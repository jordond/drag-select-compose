package dev.jordond.dragselectcompose

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import dev.jordond.dragselectcompose.DragSelectState.Companion.None

@Composable
public fun <Item> rememberDragSelectState(
    lazyGridState: LazyGridState = rememberLazyGridState(),
    initialSelection: List<Item> = emptyList(),
): DragSelectState<Item> {
    val indexes by rememberSaveable { mutableStateOf(None) }
    return remember(lazyGridState) {
        DragSelectState(lazyGridState, indexes, initialSelection)
    }
}

public class DragSelectState<Item>(
    public val lazyGridState: LazyGridState,
    internal var initialIndex: Int = -1,
    initialSelection: List<Item>,
) {

    private var selectedState by mutableStateOf(initialSelection)
    public val selected: List<Item>
        get() = selectedState

    public val inSelectionMode: Boolean
        get() = selectedState.isNotEmpty()

    internal val autoScrollSpeed = mutableStateOf(0f)

    internal fun withIndexes(
        block: DragSelectState<Item>.(initial: Int) -> Unit,
    ) {
        if (initialIndex != None) {
            block(this, initialIndex)
        }
    }

    public fun updateSelected(selected: List<Item>) {
        selectedState = selected
    }

    public fun addSelected(item: Item) {
        selectedState += item
    }

    public fun removeSelected(photo: Item) {
        selectedState -= photo
    }

    public fun clear() {
        selectedState = emptyList()
    }

    internal fun resetDrag() {
        initialIndex = None
        autoScrollSpeed.value = 0f
    }

    internal companion object {

        const val None = -1
    }
}
