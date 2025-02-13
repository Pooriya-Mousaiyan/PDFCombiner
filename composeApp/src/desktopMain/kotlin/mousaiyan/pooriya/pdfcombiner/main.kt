package mousaiyan.pooriya.pdfcombiner

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.compose.resources.painterResource
import pdfcombiner.composeapp.generated.resources.Res
import pdfcombiner.composeapp.generated.resources.pdf_combine_linux

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "PDF Combiner",
        icon = painterResource(Res.drawable.pdf_combine_linux)
    ) {
        App()
    }
}