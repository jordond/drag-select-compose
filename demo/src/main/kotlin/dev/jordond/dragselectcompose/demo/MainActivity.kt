package dev.jordond.dragselectcompose.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ClearAll
import androidx.compose.material.icons.outlined.CopyAll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.Modifier
import dev.jordond.dragselectcompose.demo.ui.theme.DragSelectComposeTheme
import dev.jordond.dragselectcompose.rememberDragSelectState

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val items = PhotoItem.createList(100)

        setContent {
            val dragSelectState = rememberDragSelectState<PhotoItem>()

            DragSelectComposeTheme {
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
    }
}
