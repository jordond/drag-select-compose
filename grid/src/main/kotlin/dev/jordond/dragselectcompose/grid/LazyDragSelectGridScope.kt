package dev.jordond.dragselectcompose.grid

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridScopeMarker
import androidx.compose.runtime.Composable
import dev.jordond.dragselectcompose.DragSelectState

@LazyGridScopeMarker
public interface LazyDragSelectGridScope<Item> {

    public fun items(
        key: ((item: Item) -> Any)? = null,
        span: (LazyGridItemSpanScope.(item: Item) -> GridItemSpan)? = null,
        contentType: (item: Item) -> Any? = { null },
        itemContent: @Composable LazyDragSelectGridItemScope<Item>.(item: Item) -> Unit,
    )
}

internal class DefaultLazyDragSelectGridScope<Item>(
    private val gridScope: LazyGridScope,
    private val items: List<Item>,
    private val stateProvider: () -> DragSelectState<Item>,
) : LazyDragSelectGridScope<Item> {

    override fun items(
        key: ((item: Item) -> Any)?,
        span: (LazyGridItemSpanScope.(item: Item) -> GridItemSpan)?,
        contentType: (item: Item) -> Any?,
        itemContent: @Composable() (LazyDragSelectGridItemScope<Item>.(item: Item) -> Unit),
    ) {
        val lazyDragSelectGridItemScope = DefaultLazyDragSelectGridItemScope(stateProvider())
        val items = this.items
        gridScope.items(
            count = items.size,
            key = if (key != null) { index: Int -> key(items[index]) } else null,
            span = if (span != null) {
                { span(items[it]) }
            } else null,
            contentType = { index: Int -> contentType(items[index]) }
        ) { index ->
            itemContent(lazyDragSelectGridItemScope, items[index])
        }
    }
}