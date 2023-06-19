package dev.jordond.dragselectcompose.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.jordond.dragselectcompose.DragSelectState
import dev.jordond.dragselectcompose.grid.LazyDragSelectVerticalGrid
import dev.jordond.dragselectcompose.grid.indicator.SelectedIcon
import dev.jordond.dragselectcompose.grid.indicator.UnselectedIcon
import dev.jordond.dragselectcompose.rememberDragSelectState
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url

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
                selectedIcon = { SelectedIcon(Modifier.align(Alignment.BottomEnd)) },
                unselectedIcon = { UnselectedIcon(Modifier.align(Alignment.BottomEnd)) },
            ) {
                Surface(
                    tonalElevation = 3.dp,
                ) {
                    KamelImage(
                        resource = asyncPainterResource(Url(photo.url)),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}