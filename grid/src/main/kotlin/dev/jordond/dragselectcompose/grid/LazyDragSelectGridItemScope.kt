package dev.jordond.dragselectcompose.grid

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridScopeMarker
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import dev.jordond.dragselectcompose.DragSelectState
import dev.jordond.dragselectcompose.extensions.dragSelectToggleableItem

/**
 * Receiver scope being used by the item content parameter of [LazyDragSelectVerticalGrid] or [LazyDragSelectHorizontalGrid].
 *
 * @param[Item] The type of the items in the grid.
 */
@LazyGridScopeMarker
public interface LazyDragSelectGridItemScope<Item> {

    /**
     * A Composable that provides selectable support for the [content].
     *
     * @param[item] The item that is being rendered.
     * @param[modifier] The modifier to be applied to the item.
     * @param[semanticsLabel] The label to be used for accessibility.
     * @param[interactionSource] [MutableInteractionSource] that will be used to
     * emit `PressInteraction.Press` when this toggleable is being pressed.
     * @param[content] The content to be rendered passes `true` if item is selected.
     */
    @Composable
    public fun SelectableItem(
        item: Item,
        modifier: Modifier,
        semanticsLabel: String,
        interactionSource: MutableInteractionSource?,
        content: @Composable (selected: Boolean) -> Unit,
    )

    /**
     * A Composable that provides selectable support for the [content].
     *
     * @param[item] The item that is being rendered.
     * @param[content] The content to be rendered passes `true` if item is selected.
     */
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

    /**
     * A Composable that provides selectable support for the [content].
     *
     * @param[item] The item that is being rendered.
     * @param[modifier] The modifier to be applied to the item.
     * @param[content] The content to be rendered passes `true` if item is selected.
     */
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

    /**
     * A Composable that provides selectable support for the [content].
     *
     * @param[item] The item that is being rendered.
     * @param[modifier] The modifier to be applied to the item.
     * @param[semanticsLabel] The label to be used for accessibility.
     * @param[content] The content to be rendered passes `true` if item is selected.
     */
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

    /**
     * This modifier animates the item placement within the Lazy grid.
     *
     * When you provide a key via [LazyGridScope.item]/[LazyGridScope.items] this modifier will
     * enable item reordering animations. Aside from item reordering all other position changes
     * caused by events like arrangement or alignment changes will also be animated.
     *
     * @param[animationSpec] a finite animation that will be used to animate the item placement.
     */
    public fun Modifier.animateItemPlacement(
        animationSpec: FiniteAnimationSpec<IntOffset> = spring(
            stiffness = Spring.StiffnessMediumLow,
            visibilityThreshold = IntOffset.VisibilityThreshold,
        ),
    ): Modifier

    private companion object {

        private const val DEFAULT_LABEL = "Select"
    }
}

/**
 * Default implementation of [LazyDragSelectGridItemScope].
 *
 * @param[Item] The type of the items in the grid.
 * @param[state] The [DragSelectState] that is being used to manage the selection.
 * @param[lazyGridItemScope] The [LazyGridItemScope] that is being used to render the items.
 */
internal class DefaultLazyDragSelectGridItemScope<Item>(
    private val state: DragSelectState<Item>,
    private val lazyGridItemScope: LazyGridItemScope,
) : LazyDragSelectGridItemScope<Item> {

    /**
     * @see LazyDragSelectGridItemScope.SelectableItem
     */
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

    /**
     * @see LazyDragSelectGridItemScope.animateItemPlacement
     */
    @OptIn(ExperimentalFoundationApi::class)
    override fun Modifier.animateItemPlacement(
        animationSpec: FiniteAnimationSpec<IntOffset>,
    ): Modifier = lazyGridItemScope.run {
        this@animateItemPlacement.animateItemPlacement(animationSpec)
    }
}