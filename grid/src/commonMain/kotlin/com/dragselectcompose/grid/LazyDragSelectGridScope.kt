package com.dragselectcompose.grid

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridScopeMarker
import androidx.compose.runtime.Composable
import com.dragselectcompose.core.DragSelectState

/**
 * Receiver scope for [LazyDragSelectHorizontalGrid] and [LazyDragSelectVerticalGrid].
 *
 * This is essentially a copy of [LazyGridScope].
 */
@LazyGridScopeMarker
public interface LazyDragSelectGridScope<Item> {

    /**
     * Adds a single item to the scope.
     *
     * Wrapper around [LazyGridScope.item].
     *
     * @see LazyGridScope.item
     * @param[key] a stable and unique key representing the item. Using the same key for multiple
     * items in the grid is not allowed. Type of the key should be saveable via Bundle on Android.
     * If null is passed the position in the grid will represent the key. When you specify the key
     * the scroll position will be maintained based on the key, which means if you add/remove items
     * before the current visible item the item with the given key will be kept as the first visible one.
     * @param[span] the span of the item. Default is 1x1. It is good practice to leave it `null`
     * when this matches the intended behavior, as providing a custom implementation impacts performance.
     * @param[contentType] the type of the content of this item. The item compositions of the same
     * type could be reused more efficiently. Note that null is a valid type and items of such
     * type will be considered compatible.
     * @param[content] the content of the item.
     */
    public fun item(
        key: Any? = null,
        span: (LazyGridItemSpanScope.() -> GridItemSpan)? = null,
        contentType: Any? = null,
        content: @Composable LazyDragSelectGridItemScope<Item>.() -> Unit,
    )

    /**
     * Adds a list of items from the [Item]s in the scope.
     *
     * Wrapper around [LazyGridScope.items].
     *
     * @see LazyGridScope.items
     * @param[span] define custom spans for the items. Default is 1x1. It is good practice to leave
     * it null when this matches the intended behavior, as providing a custom implementation
     * impacts performance.
     * @param[contentType] a factory of the content types for the item. The item compositions of
     * the same type could be reused more efficiently. Note that null is a valid type and items of
     * such type will be considered compatible.
     * @param[itemContent] the content displayed by a single item.
     */
    public fun items(
        span: (LazyGridItemSpanScope.(item: Item) -> GridItemSpan)? = null,
        contentType: (item: Item) -> Any? = { null },
        itemContent: @Composable LazyDragSelectGridItemScope<Item>.(item: Item) -> Unit,
    )
}

/**
 * Implementation of [LazyDragSelectGridScope] that wraps a [LazyGridScope].
 *
 * @param[gridScope] The [LazyGridScope] to wrap.
 * @param[items] The list of items to display.
 * @param[stateProvider] A function that returns the [DragSelectState] for this grid.
 */
internal class DefaultLazyDragSelectGridScope<Item>(
    private val gridScope: LazyGridScope,
    private val items: List<Item>,
    private val stateProvider: () -> DragSelectState<Item>,
) : LazyDragSelectGridScope<Item> {

    /**
     * Adds a single item to the scope.
     *
     * @see[LazyGridScope.item]
     * @see[LazyDragSelectGridScope.item]
     */
    override fun item(
        key: Any?,
        span: (LazyGridItemSpanScope.() -> GridItemSpan)?,
        contentType: Any?,
        content: @Composable() (LazyDragSelectGridItemScope<Item>.() -> Unit),
    ) {
        gridScope.item(key, span, contentType) {
            val scope = LazyDragSelectGridItemScope(
                state = this@DefaultLazyDragSelectGridScope.stateProvider(),
                lazyGridItemScope = this,
            )
            content(scope)
        }
    }

    /**
     * Adds [items] to the scope.
     *
     * @see[LazyGridScope.items]
     * @see[LazyDragSelectGridScope.items]
     */
    override fun items(
        span: (LazyGridItemSpanScope.(item: Item) -> GridItemSpan)?,
        contentType: (item: Item) -> Any?,
        itemContent: @Composable() (LazyDragSelectGridItemScope<Item>.(item: Item) -> Unit),
    ) {
        val items = this.items
        gridScope.items(
            count = items.size,
            key = { index: Int -> stateProvider().key(items[index]) },
            span = if (span != null) {
                { span(items[it]) }
            } else null,
            contentType = { index: Int -> contentType(items[index]) }
        ) { index ->
            val scope = LazyDragSelectGridItemScope(
                state = this@DefaultLazyDragSelectGridScope.stateProvider(),
                lazyGridItemScope = this,
            )
            itemContent(scope, items[index])
        }
    }
}