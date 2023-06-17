package dev.jordond.dragselectcompose.extensions

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import dev.jordond.dragselectcompose.DragSelectState

/**
 * A convenience function for creating a [Modifier] that will toggle the selection state of an item
 * in a [DragSelectState]. As well as add the long-click semantics to the item.
 *
 * @see Modifier.dragSelectSemantics
 * @see Modifier.dragSelectToggleable
 * @param[Item] The type of item in the [DragSelectState].
 * @param[dragSelectState] The [DragSelectState] to toggle the selection state of the item in.
 * @param[item] The item to toggle the selection state of.
 * @param[semanticsLabel] The label to use for the long-click semantics.
 * @param[interactionSource] The [MutableInteractionSource] that will be used to
 * emit `PressInteraction.Press` when this toggleable is being pressed.
 */
public fun <Item> Modifier.dragSelectToggleableItem(
    dragSelectState: DragSelectState<Item>,
    item: Item,
    semanticsLabel: String = DEFAULT_LABEL,
    interactionSource: MutableInteractionSource? = null,
): Modifier = then(
    Modifier
        .dragSelectSemantics(dragSelectState, item, semanticsLabel)
        .dragSelectToggleable(dragSelectState, item, interactionSource)
)