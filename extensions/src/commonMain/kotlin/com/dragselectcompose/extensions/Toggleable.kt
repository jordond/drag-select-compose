package com.dragselectcompose.extensions

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import com.dragselectcompose.core.DragSelectState

/**
 * A toggleable modifier that is only enabled when [DragSelectState.inSelectionMode] is true.
 *
 * This is useful for enabling selection when the user is in selection mode.
 *
 * @param[state] The [DragSelectState] that will be used to determine if the user is
 * in selection mode and selection state of item.
 * @param[item] The item that will be selected or deselected when the toggleable is toggled.
 * @param[interactionSource] the [MutableInteractionSource] that will be used to
 * emit `PressInteraction.Press` when this toggleable is being pressed.
 */
public fun <Item> Modifier.dragSelectToggleable(
    state: DragSelectState<Item>,
    item: Item,
    interactionSource: MutableInteractionSource? = null,
): Modifier = dragSelectToggleable(
    inSelectionMode = state.inSelectionMode,
    selected = state.selected.contains(item),
    interactionSource = interactionSource,
) { toggled ->
    if (toggled) state.addSelected(item)
    else state.removeSelected(item)
}


/**
 * A toggleable modifier that is only enabled when [inSelectionMode] is true.
 *
 * This is useful for enabling selection when the user is in selection mode.
 *
 * @param[inSelectionMode] Whether the user is in selection mode.
 * @param[selected] Whether the item is selected.
 * @param[interactionSource] the [MutableInteractionSource] that will be used to
 * emit `PressInteraction.Press` when this toggleable is being pressed.
 * @param[onToggle] Called when the toggleable is toggled.
 */
public fun Modifier.dragSelectToggleable(
    inSelectionMode: Boolean,
    selected: Boolean,
    interactionSource: MutableInteractionSource? = null,
    onToggle: (toggled: Boolean) -> Unit,
): Modifier = composed {
    val interaction = interactionSource ?: remember { MutableInteractionSource() }

    if (!inSelectionMode) Modifier
    else then(
        Modifier.toggleable(
            value = selected,
            interactionSource = interaction,
            indication = null,
            onValueChange = onToggle
        ),
    )
}