package dev.jordond.dragselectcompose.extensions

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Modifier
import dev.jordond.dragselectcompose.DragSelectState

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