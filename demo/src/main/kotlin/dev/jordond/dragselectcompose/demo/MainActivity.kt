package dev.jordond.dragselectcompose.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dev.jordond.dragselectcompose.demo.ui.theme.DragSelectComposeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DragSelectComposeTheme {
                PhotoGrid()
            }
        }
    }
}
