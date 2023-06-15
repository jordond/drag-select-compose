package dev.jordond.dragselectcompose

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import java.io.Serializable

@Composable
public fun <Item> rememberGridDragSelectState(
    lazyGridState: LazyGridState,
    initialSelection: List<Item> = emptyList(),
): GridDragSelectState<Item> {
    val indexes by rememberSaveable { mutableStateOf(GridDragSelectState.Indexes()) }
    return remember {
        GridDragSelectState(lazyGridState, indexes, initialSelection)
    }
}

public class GridDragSelectState<Item>(
    internal val lazyGridState: LazyGridState,
    private var indexes: Indexes,
    initialSelection: List<Item>,
) {

    private val selectedState = mutableStateOf(initialSelection)
    public val selected: List<Item>
        get() = selectedState.value

    public val inSelectionMode: Boolean
        get() = selectedState.value.isNotEmpty()

    internal val autoScrollSpeed = mutableStateOf(0f)

    internal fun withIndexes(
        block: GridDragSelectState<Item>.(initial: Int, current: Int) -> Unit,
    ) {
        if (indexes.valid) {
            val (initial, current) = indexes
            block(this, initial, current)
        }
    }

    public fun updateSelected(selected: List<Item>) {
        selectedState.value = selected
    }

    public fun addSelected(item: Item) {
        selectedState.value += item
    }

    public fun removeSelected(photo: Item) {
        selectedState.value -= photo
    }

    internal fun initializeIndexes(initial: Int, current: Int) {
        indexes = Indexes(initial, current)
    }

    internal fun updateCurrentIndex(index: Int) {
        indexes = indexes.copy(current = index)
    }

    internal fun resetDrag() {
        indexes = indexes.copy(initial = Indexes.None)
        autoScrollSpeed.value = 0f
    }

    public data class Indexes(
        val initial: Int = None,
        val current: Int = None,
    ) : Serializable {

        internal val valid: Boolean = initial != None && current != None

        internal companion object {

            internal val None = -1
        }
    }
}
