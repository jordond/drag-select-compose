package com.dragselectcompose.grid

import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScopeMarker
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.dragselectcompose.core.DragSelectState
import com.dragselectcompose.extensions.dragSelectToggleableItem
import com.dragselectcompose.grid.indicator.AnimateSelectionDefaults
import com.dragselectcompose.grid.indicator.AnimateSelectionOptions
import com.dragselectcompose.grid.indicator.SelectedIcon
import com.dragselectcompose.grid.indicator.UnselectedIcon

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
     * @param[enableIconIndicator] Whether to enable the selected indicator.
     * @param[selectedIcon] The icon to be rendered when the item is selected.
     * @param[unselectedIcon] The icon to be rendered when the item is not selected.
     * @param[animateSelection] Whether to animate the padding and shape when selected.
     * @param[animateSelectionOptions] The options to be used when animating the selection.
     * @param[content] The content to be rendered passes `true` if item is selected.
     */
    @Composable
    public fun SelectableItem(
        item: Item,
        modifier: Modifier = Modifier,
        semanticsLabel: String = DEFAULT_LABEL,
        interactionSource: MutableInteractionSource? = null,
        enableIconIndicator: Boolean = true,
        selectedIcon: @Composable BoxScope.() -> Unit = { SelectedIcon() },
        unselectedIcon: @Composable BoxScope.() -> Unit = { UnselectedIcon() },
        animateSelection: Boolean = true,
        animateSelectionOptions: AnimateSelectionOptions = AnimateSelectionDefaults.Default,
        aspectRatio: Float = 1.0f,
        content: @Composable (toggled: Boolean) -> Unit,
    ) {
        val selected by remember { derivedStateOf { state.isSelected(item) } }

        // Add the semantics and toggleable item modifier to the content.
        Box(
            modifier = modifier
                .dragSelectToggleableItem(
                    state = state,
                    item = item,
                    semanticsLabel = semanticsLabel,
                    interactionSource = interactionSource,
                ),
        ) {
            val contentModifier =
                if (!animateSelection) Modifier
                else Modifier.animateSelection(selected, animateSelectionOptions)

            // Animate the padding and shape when `animateSelection` is true.
            Box(
                propagateMinConstraints = true,
                contentAlignment = Alignment.Center,
                modifier = contentModifier.aspectRatio(aspectRatio),
            ) {
                content(selected)
            }

            // Display the indicator icons if `enableIconIndicator` is true.
            if (enableIconIndicator && state.inSelectionMode) {
                Box(modifier = Modifier.matchParentSize()) {
                    if (selected) {
                        selectedIcon(this)
                    } else {
                        unselectedIcon(this)
                    }
                }
            }
        }
    }

    /**
     * @see LazyGridItemScope.animateItem
     */
    @OptIn(ExperimentalFoundationApi::class)
    public fun Modifier.animateItem(
        fadeInSpec: FiniteAnimationSpec<Float>? = spring(stiffness = Spring.StiffnessMediumLow),
        placementSpec: FiniteAnimationSpec<IntOffset>? =
            spring(
                stiffness = Spring.StiffnessMediumLow,
                visibilityThreshold = IntOffset.VisibilityThreshold
            ),
        fadeOutSpec: FiniteAnimationSpec<Float>? = spring(stiffness = Spring.StiffnessMediumLow),
    ): Modifier = lazyGridItemScope.run {
        this@animateItem.animateItem(fadeInSpec, placementSpec, fadeOutSpec)
    }

    /**
     * Animate the padding and shape when [selected] changes value.
     *
     * @param[selected] Whether the item is selected.
     * @param[options] The options to be used when animating the selection.
     */
    private fun Modifier.animateSelection(
        selected: Boolean,
        options: AnimateSelectionOptions = AnimateSelectionDefaults.Default,
    ): Modifier = composed {
        val transition = updateTransition(selected, label = "selected")
        val padding by transition.animateDp(label = "padding") { selected ->
            if (selected) options.padding else 0.dp
        }
        val roundedCornerShape by transition.animateDp(label = "corner") { selected ->
            if (selected) options.cornerRadius else 0.dp
        }

        this
            .padding(padding)
            .clip(RoundedCornerShape(roundedCornerShape))
    }

    private companion object {

        private const val DEFAULT_LABEL = "Select"
    }
}