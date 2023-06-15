package dev.jordond.dragselectcompose.extensions

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import dev.jordond.dragselectcompose.DragSelectState
import dev.jordond.dragselectcompose.extensions.internal.conditional

public fun <Item> Modifier.dragSelectToggleable(
    dragSelectState: DragSelectState<Item>,
    item: Item,
    interactionSource: MutableInteractionSource? = null,
): Modifier = dragSelectToggleable(
    inSelectionMode = dragSelectState.inSelectionMode,
    selected = dragSelectState.selected.contains(item),
    interactionSource = interactionSource,
) { toggled ->
    if (toggled) dragSelectState.addSelected(item)
    else dragSelectState.removeSelected(item)
}


public fun Modifier.dragSelectToggleable(
    inSelectionMode: Boolean,
    selected: Boolean,
    interactionSource: MutableInteractionSource? = null,
    onToggle: (toggled: Boolean) -> Unit,
): Modifier = composed {
    val interaction = interactionSource ?: remember { MutableInteractionSource() }

    conditional(inSelectionMode) {
        toggleable(
            value = selected,
            interactionSource = interaction,
            indication = null,
            onValueChange = onToggle
        )
    }
}