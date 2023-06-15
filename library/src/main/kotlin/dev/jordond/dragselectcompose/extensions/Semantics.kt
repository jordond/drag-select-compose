package dev.jordond.dragselectcompose.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.onLongClick
import androidx.compose.ui.semantics.semantics
import dev.jordond.dragselectcompose.DragSelectState

internal const val DEFAULT_LABEL = "Select"

public fun <Item> Modifier.dragSelectSemantics(
    dragSelectState: DragSelectState<Item>,
    label: String = DEFAULT_LABEL,
    onLongClick: () -> Unit,
): Modifier = dragSelectSemantics(dragSelectState.inSelectionMode, label, onLongClick)

public fun <Item> Modifier.dragSelectSemantics(
    dragSelectState: DragSelectState<Item>,
    item: Item,
    label: String = DEFAULT_LABEL,
): Modifier = dragSelectSemantics(dragSelectState, label) {
    dragSelectState.addSelected(item)
}

public fun Modifier.dragSelectSemantics(
    inSelectionMode: Boolean,
    label: String = DEFAULT_LABEL,
    onLongClick: () -> Unit,
): Modifier = then(
    Modifier.semantics {
        if (!inSelectionMode) {
            onLongClick(label = label) {
                onLongClick()
                true
            }
        }
    },
)
