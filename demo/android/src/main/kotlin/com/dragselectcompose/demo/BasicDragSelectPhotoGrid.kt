package com.dragselectcompose.demo

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.onLongClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.dragselectcompose.core.DragSelectState
import com.dragselectcompose.demo.ui.theme.DragSelectComposeTheme
import com.dragselectcompose.core.gridDragSelect
import com.dragselectcompose.core.rememberDragSelectState

/**
 * A basic example on how to use [Modifier.gridDragSelect] with a [LazyVerticalGrid].
 *
 * This is a bare-bones example and you are responsible for displaying the selection state of the
 * item. As well as adding semantics and toggleable modifiers to the item.
 *
 * If you want a more "everything included" solution, see [BasicDragSelectPhotoGrid].
 */
@Composable
fun BasicDragSelectPhotoGrid(
    modifier: Modifier = Modifier,
    photoItems: List<PhotoItem> = PhotoItem.createList(100),
    dragSelectState: DragSelectState<PhotoItem> = rememberDragSelectState(),
) {
    LazyVerticalGrid(
        state = dragSelectState.gridState,
        columns = GridCells.Adaptive(minSize = 128.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        modifier = modifier.gridDragSelect(
            items = photoItems,
            state = dragSelectState,
        ),
    ) {
        items(photoItems, key = { it.id }) { item ->
            val selected by remember { derivedStateOf { dragSelectState.isSelected(item) } }
            PhotoItemContent(
                item = item,
                inSelectionMode = dragSelectState.inSelectionMode,
                selected = selected,
                modifier = Modifier
                    // Add semantics for accessibility
                    .semantics {
                        if (!dragSelectState.inSelectionMode) {
                            onLongClick("Select") {
                                dragSelectState.addSelected(item)
                                true
                            }
                        }
                    }
                    // If we are in selection mode allow the item to be toggleable
                    .then(
                        if (!dragSelectState.inSelectionMode) Modifier
                        else {
                            Modifier.toggleable(
                                value = selected,
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null, // do not show a ripple
                                onValueChange = { toggled ->
                                    if (toggled) dragSelectState.addSelected(item)
                                    else dragSelectState.removeSelected(item)
                                }
                            )
                        },
                    )
            )
        }
    }
}

@Composable
private fun PhotoItemContent(
    item: PhotoItem,
    inSelectionMode: Boolean,
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.aspectRatio(1f),
        tonalElevation = 3.dp
    ) {
        Box {
            val transition = updateTransition(selected, label = "selected")
            val padding by transition.animateDp(label = "padding") { selected ->
                if (selected) 10.dp else 0.dp
            }
            val roundedCornerShape by transition.animateDp(label = "corner") { selected ->
                if (selected) 16.dp else 0.dp
            }
            Image(
                painter = rememberAsyncImagePainter(item.url),
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    .padding(padding)
                    .clip(RoundedCornerShape(roundedCornerShape))
            )
            if (inSelectionMode) {
                if (selected) {
                    val backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                    Icon(
                        Icons.Filled.CheckCircle,
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(4.dp)
                            .border(2.dp, backgroundColor, CircleShape)
                            .clip(CircleShape)
                            .background(backgroundColor)
                    )
                } else {
                    Icon(
                        Icons.Filled.RadioButtonUnchecked,
                        tint = Color.White.copy(alpha = 0.7f),
                        contentDescription = null,
                        modifier = Modifier.padding(6.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun BasicDragSelectPhotoGridPreview() {
    DragSelectComposeTheme {
        BasicDragSelectPhotoGrid()
    }
}