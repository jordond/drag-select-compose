package com.dragselectcompose.demo

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.material.icons.outlined.CopyAll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dragselectcompose.core.rememberDragSelectState

@Composable
fun App() {
    val allItems by remember { mutableStateOf(PhotoItem.createList(100)) }
    var selectedCategory by remember { mutableStateOf<PhotoCategory?>(null) }
    val dragSelectState = rememberDragSelectState<PhotoItem>(
        compareSelector = { it.id },
    )

    val filteredItems = remember(allItems, selectedCategory) {
        if (selectedCategory == null) {
            allItems
        } else {
            allItems.filter { it.category == selectedCategory }
        }
    }

    LaunchedEffect(filteredItems) {
        dragSelectState.reconcile(filteredItems)
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        if (dragSelectState.inSelectionMode) {
                            val size = dragSelectState.selected.size
                            Text(text = "Selected: $size")
                        } else {
                            Text(text = "Drag Select Demo")
                        }
                    },
                    actions = {
                        Button(
                            onClick = {
                                dragSelectState.toggleSelectionMode()
                            },
                        ) {
                            val text = if (dragSelectState.inSelectionMode) "Cancel" else "Select"
                            Text(text = text)
                        }

                        IconButton(onClick = { dragSelectState.updateSelected(filteredItems) }) {
                            Icon(
                                imageVector = Icons.Outlined.CopyAll,
                                contentDescription = null,
                            )
                        }

                        IconButton(onClick = { dragSelectState.disableSelectionMode() }) {
                            Icon(
                                imageVector = Icons.Outlined.ClearAll,
                                contentDescription = null,
                            )
                        }
                    }
                )
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { selectedCategory = null },
                        label = { Text("All") },
                    )
                    PhotoCategory.entries.forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { selectedCategory = category },
                            label = { Text(category.name) },
                        )
                    }
                }

                PhotoGrid(
                    dragSelectState = dragSelectState,
                    photoItems = filteredItems,
                )
            }
        }
    }
}