package dev.jordond.dragselectcompose.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.onLongClick
import androidx.compose.ui.semantics.semantics
import dev.jordond.dragselectcompose.DragSelectState

internal const val DEFAULT_LABEL = "Select"

/**
 * Adds a long click semantics to the modifier.
 *
 * When the semantics is triggered, the [item] is added to the selection in [DragSelectState].
 *
 * @param[Item] the type of the items in the selection.
 * @param[dragSelectState] the state to use for the semantics.
 * @param[item] the item to add to the selection when the semantics is triggered.
 * @param[label] the label to use for the semantics.
 */
public fun <Item> Modifier.dragSelectSemantics(
    dragSelectState: DragSelectState<Item>,
    item: Item,
    label: String = DEFAULT_LABEL,
): Modifier = dragSelectSemantics(dragSelectState, label) {
    dragSelectState.addSelected(item)
}

/**
 * Adds a long click semantics to the modifier.
 *
 * @param[Item] the type of the items in the selection.
 * @param[dragSelectState] the state to use for the semantics.
 * @param[label] the label to use for the semantics.
 * @param[onLongClick] the callback to invoke when the semantics is triggered.
 */
public fun <Item> Modifier.dragSelectSemantics(
    dragSelectState: DragSelectState<Item>,
    label: String = DEFAULT_LABEL,
    onLongClick: () -> Unit,
): Modifier = dragSelectSemantics(dragSelectState.inSelectionMode, label, onLongClick)

/**
 * Adds a long click semantics to the modifier.
 *
 * @param[inSelectionMode] whether the semantics should be added or not.
 * @param[label] the label to use for the semantics.
 * @param[onLongClick] the callback to invoke when the semantics is triggered.
 */
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
