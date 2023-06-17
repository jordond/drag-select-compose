package dev.jordond.dragselectcompose.grid

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScopeMarker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import dev.jordond.dragselectcompose.DragSelectState
import dev.jordond.dragselectcompose.extensions.dragSelectToggleableItem
import dev.jordond.dragselectcompose.grid.indicator.SelectedIcon
import dev.jordond.dragselectcompose.grid.indicator.UnselectedIcon

/**
 * Receiver scope being used by the item content parameter of [LazyDragSelectVerticalGrid] or [LazyDragSelectHorizontalGrid].
 *
 * @param[Item] The type of the items in the grid.
 * @param[state] The [DragSelectState] that is being used to manage the selection.
 * @param[lazyGridItemScope] The [LazyGridItemScope] that is being used to render the items.
 */
@LazyGridScopeMarker
public class LazyDragSelectGridItemScope<Item>(
    private val state: DragSelectState<Item>,
    private val lazyGridItemScope: LazyGridItemScope,
) {

    /**
     * A Composable that provides selectable support for the [content].
     *
     * @param[item] The item that is being rendered.
     * @param[modifier] The modifier to be applied to the item.
     * @param[semanticsLabel] The label to be used for accessibility.
     * @param[interactionSource] [MutableInteractionSource] that will be used to
     * emit `PressInteraction.Press` when this toggleable is being pressed.
     * @param[enableIndicator] Whether to enable the selected indicator.
     * @param[selectedIcon] The icon to be rendered when the item is selected.
     * @param[unselectedIcon] The icon to be rendered when the item is not selected.
     * @param[content] The content to be rendered passes `true` if item is selected.
     */
    @Composable
    public fun SelectableItem(
        item: Item,
        modifier: Modifier = Modifier,
        semanticsLabel: String = DEFAULT_LABEL,
        interactionSource: MutableInteractionSource? = null,
        enableIndicator: Boolean = true,
        selectedIcon: @Composable BoxScope.() -> Unit = { SelectedIcon() },
        unselectedIcon: @Composable BoxScope.() -> Unit = { UnselectedIcon() },
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

            if (enableIndicator && state.inSelectionMode) {
                if (selected) {
                    selectedIcon(this)
                } else {
                    unselectedIcon(this)
                }
            }
        }
    }

    /**
     * @see LazyDragSelectGridItemScope.animateItemPlacement
     */
    @OptIn(ExperimentalFoundationApi::class)
    public fun Modifier.animateItemPlacement(
        animationSpec: FiniteAnimationSpec<IntOffset>,
    ): Modifier = lazyGridItemScope.run {
        this@animateItemPlacement.animateItemPlacement(animationSpec)
    }

    private companion object {

        private const val DEFAULT_LABEL = "Select"
    }
}