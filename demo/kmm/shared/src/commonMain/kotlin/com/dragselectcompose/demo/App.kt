package com.dragselectcompose.demo

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.material.icons.outlined.CopyAll
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.dragselectcompose.core.rememberDragSelectState

@Composable
fun App() {
    val items by remember { mutableStateOf(PhotoItem.createList(100)) }
    val dragSelectState = rememberDragSelectState<PhotoItem>()

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
                                if (dragSelectState.inSelectionMode) dragSelectState.clear()
                                else dragSelectState.enableSelectionMode()
                            },
                        ) {
                            val text = if (dragSelectState.inSelectionMode) "Cancel" else "Select"
                            Text(text = text)
                        }

                        IconButton(onClick = { dragSelectState.updateSelected(items) }) {
                            Icon(
                                imageVector = Icons.Outlined.CopyAll,
                                contentDescription = null,
                            )
                        }

                        IconButton(onClick = { dragSelectState.clear() }) {
                            Icon(
                                imageVector = Icons.Outlined.ClearAll,
                                contentDescription = null,
                            )
                        }
                    }
                )
            }
        ) { padding ->
            PhotoGrid(
                dragSelectState = dragSelectState,
                photoItems = items,
                modifier = Modifier.padding(padding)
            )
        }
    }
}