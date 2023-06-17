package dev.jordond.dragselectcompose.demo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
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

@Composable
fun PhotoGrid(
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
                animateSelection = false,
                selectedIcon = { SelectedIcon(Modifier.align(Alignment.BottomEnd)) },
                unselectedIcon = { UnselectedIcon(Modifier.align(Alignment.BottomEnd)) },
            ) {
                Surface(
                    modifier = modifier.aspectRatio(1f),
                    tonalElevation = 3.dp
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(photo.url),
                        contentDescription = null,
                        modifier = Modifier
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PhotoGridPreview() {
    DragSelectComposeTheme {
        PhotoGrid()
    }
}