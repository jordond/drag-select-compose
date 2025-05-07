package com.dragselectcompose.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.dragselectcompose.core.DragSelectState
import com.dragselectcompose.core.rememberDragSelectState
import com.dragselectcompose.grid.LazyDragSelectVerticalGrid
import com.dragselectcompose.grid.indicator.SelectedIcon
import com.dragselectcompose.grid.indicator.UnselectedIcon

@Composable
fun PhotoGrid(
    modifier: Modifier = Modifier,
    photoItems: List<PhotoItem> = PhotoItem.createList(100),
    dragSelectState: DragSelectState<PhotoItem> = rememberDragSelectState(compareSelector = { it.id }),
) {
    LazyDragSelectVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 128.dp),
        items = photoItems,
        state = dragSelectState,
        verticalArrangement = Arrangement.spacedBy(3.dp),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        items { photo ->
            SelectableItem(
                item = photo,
                selectedIcon = { SelectedIcon(Modifier.align(Alignment.BottomEnd)) },
                unselectedIcon = { UnselectedIcon(Modifier.align(Alignment.BottomEnd)) },
            ) {
                Surface(
                    tonalElevation = 3.dp,
                ) {
                    AsyncImage(
                        model = photo.url,
                        onError = {
                            println("Error loading image: ${it.result.throwable.stackTraceToString()}")
                        },
                        contentDescription = null,
                    )
                }
            }
        }
    }
}