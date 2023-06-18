package dev.jordond.dragselectcompose.demo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import dev.jordond.dragselectcompose.DragSelectState
import dev.jordond.dragselectcompose.demo.ui.theme.DragSelectComposeTheme
import dev.jordond.dragselectcompose.grid.LazyDragSelectVerticalGrid
import dev.jordond.dragselectcompose.grid.indicator.SelectedIcon
import dev.jordond.dragselectcompose.grid.indicator.UnselectedIcon
import dev.jordond.dragselectcompose.rememberDragSelectState

/**
 * This example shows how to use the [LazyDragSelectVerticalGrid] along with `SelectableItem`.
 *
 * The adding of Semantics is automatic, and so is adding the Toggleable modifier to the items when
 * [DragSelectState.inSelectionMode] is true.
 *
 * `SelectableItem` takes care of rendering an icon when the item is selected, and animating the
 * content when selected or deselected.
 */
@Composable
fun LazyDragSelectPhotoGrid(
    modifier: Modifier = Modifier,
    photoItems: List<PhotoItem> = PhotoItem.createList(100),
    dragSelectState: DragSelectState<PhotoItem> = rememberDragSelectState(),
) {
    LazyDragSelectVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 128.dp),
        items = photoItems,
        state = dragSelectState,
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        items(key = { it.id }) { photo ->
            SelectableItem(
                item = photo,
                selectedIcon = { SelectedIcon(Modifier.align(Alignment.BottomEnd)) },
                unselectedIcon = { UnselectedIcon(Modifier.align(Alignment.BottomEnd)) },
            ) {
                Surface(
                    tonalElevation = 3.dp,
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(photo.url),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun LazyDragSelectPhotoGridPreview() {
    DragSelectComposeTheme {
        LazyDragSelectPhotoGrid()
    }
}