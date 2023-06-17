package dev.jordond.dragselectcompose.demo

import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
            ) { selected ->
                ImageItem(
                    photo = photo,
                    selected = selected,
                )
            }
        }
    }
}

@Composable
private fun ImageItem(
    photo: PhotoItem,
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
                painter = rememberAsyncImagePainter(photo.url),
                contentDescription = null,
                modifier = Modifier
                    .matchParentSize()
                    .padding(padding)
                    .clip(RoundedCornerShape(roundedCornerShape))
            )
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