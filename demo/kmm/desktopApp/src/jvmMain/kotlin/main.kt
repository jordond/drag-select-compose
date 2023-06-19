import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.dragselectcompose.demo.MainView

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        MainView()
    }
}