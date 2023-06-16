package dev.jordond.dragselectcompose.grid

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import dev.jordond.dragselectcompose.DragSelectState
import dev.jordond.dragselectcompose.extensions.dragSelectToggleableItem

public interface LazyDragSelectGridItemScope<Item> {

    @Composable
    public fun SelectableItem(
        item: Item,
        modifier: Modifier,
        semanticsLabel: String,
        interactionSource: MutableInteractionSource?,
        content: @Composable (selected: Boolean) -> Unit,
    )

    @Composable
    public fun SelectableItem(
        item: Item,
        content: @Composable (selected: Boolean) -> Unit,
    ): Unit = SelectableItem(
        item = item,
        modifier = Modifier,
        semanticsLabel = DEFAULT_LABEL,
        content = content,
    )

    @Composable
    public fun SelectableItem(
        item: Item,
        modifier: Modifier,
        content: @Composable (selected: Boolean) -> Unit,
    ): Unit = SelectableItem(
        item = item,
        modifier = modifier,
        semanticsLabel = DEFAULT_LABEL,
        content = content,
    )

    @Composable
    public fun SelectableItem(
        item: Item,
        modifier: Modifier,
        semanticsLabel: String,
        content: @Composable (selected: Boolean) -> Unit,
    ): Unit = SelectableItem(
        item = item,
        modifier = modifier,
        semanticsLabel = semanticsLabel,
        interactionSource = null,
        content = content,
    )

    private companion object {

        private const val DEFAULT_LABEL = "Select"
    }
}

internal class DefaultLazyDragSelectGridItemScope<Item>(
    private val state: DragSelectState<Item>,
) : LazyDragSelectGridItemScope<Item> {

    @Composable
    override fun SelectableItem(
        item: Item,
        modifier: Modifier,
        semanticsLabel: String,
        interactionSource: MutableInteractionSource?,
        content: @Composable (toggled: Boolean) -> Unit,
    ) {
        val selected by remember { derivedStateOf { state.selected.contains(item) } }
        Box(
            modifier = modifier.dragSelectToggleableItem(
                dragSelectState = state,
                item = item,
                semanticsLabel = semanticsLabel,
                interactionSource = interactionSource,
            ),
        ) {
            content(selected)
        }
    }
}