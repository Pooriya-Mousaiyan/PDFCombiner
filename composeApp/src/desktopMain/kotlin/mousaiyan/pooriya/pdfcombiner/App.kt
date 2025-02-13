package mousaiyan.pooriya.pdfcombiner

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.formdev.flatlaf.FlatLightLaf
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.utils.PdfMerger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.pdfbox.pdmodel.PDDocument
import org.jetbrains.compose.ui.tooling.preview.Preview
import java.io.File
import javax.swing.JFileChooser
import javax.swing.UIManager
import javax.swing.filechooser.FileNameExtensionFilter

@Composable
@Preview
fun App() {
    MaterialTheme {
        val allPdfData = remember { mutableStateListOf<PDFData>() }
        val scope = rememberCoroutineScope()
        var errorMessage by remember { mutableStateOf("") }
        val showErrorMessage = remember { mutableStateOf(false) }
        var successMessage by remember { mutableStateOf("") }
        val showSuccessMessage = remember { mutableStateOf(false) }
        val showOnProgressBox = remember { mutableStateOf(false) }

        // Because all OS does not have good compatibility with this library
        if (System.getProperty("os.name").lowercase().contains("windows")) {
            UIManager.setLookAndFeel(FlatLightLaf())
        }


        val makeErrorWindow: (String) -> Unit = { text ->
            errorMessage = text
            showErrorMessage.value = true
        }

        val makeSuccessWindow: (String) -> Unit = { text ->
            successMessage = text
            showSuccessMessage.value = true
        }

        Box(modifier = Modifier.padding(12.dp)) {


            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                PDFContainer {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        items(
                            count = allPdfData.toList().size,
                            key = { index: Int ->
                                Pair(index, allPdfData[index].name)
                            }
                        ) { index ->
                            val pdf = allPdfData[index]
                            PDFBox(
                                pdf,
                                onDeleteClick = {
                                    allPdfData.remove(pdf)
                                }
                            )
                        }

                        item {
                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = Color(0xff0053d9),
                                    contentColor = Color.White
                                ),
                                onClick = {
                                    scope.launch(Dispatchers.IO) {
                                        pdfRequest(
                                            onSuccess = { pdf ->
                                                if (pdf != null) {
                                                    scope.launch(Dispatchers.Main) {
                                                        allPdfData.add(pdf)
                                                    }
                                                }
                                            },
                                            onFail = { error ->
                                                scope.launch(Dispatchers.Main) {
                                                    makeErrorWindow(error.message.toString())
                                                }
                                            }
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    null
                                )
                                Text("Add PDF")
                            }
                        }
                    }
                }
                NewPDFGeneratorBox(
                    onLocationChooseButtonClicked = { savePath ->
                        scope.launch(Dispatchers.IO) {
                            try {
                                val fileChooser = JFileChooser().apply {
                                    dialogTitle = "Save to"
                                    fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                                }
                                val chosenDirectory = fileChooser.showOpenDialog(null)
                                if (chosenDirectory == JFileChooser.APPROVE_OPTION) {
                                    savePath.value = fileChooser.selectedFile.absolutePath
                                }
                            } catch (e: Exception) {
                                scope.launch(Dispatchers.Main) {
                                    makeErrorWindow(e.message.toString())
                                }
                            }
                        }
                    },
                    onSaveButtonClicked = { name, savedPath ->
                        try {
                            when {
                                allPdfData.isEmpty() -> throw Exception("Choose at least one pdf!")
                                name.isEmpty() -> throw Exception("Name field shouldn't be empty!")
                                savedPath.isEmpty() -> throw Exception("Save Path field shouldn't be empty!")
                            }
                            showOnProgressBox.value = true
                            scope.launch(Dispatchers.IO) {
                                val correctFileName = when {
                                    name.contains(".pdf") -> name.removeSuffix(".pdf")
                                    name.contains(".PDF") -> name.removeSuffix(".pdf")
                                    else -> name
                                }
                                createNewPdf(
                                    correctFileName,
                                    savedPath,
                                    allPdfData,
                                    onError = { e: Exception ->
                                        makeErrorWindow(e.message.toString())
                                    })
                                scope.launch(Dispatchers.Main) {
                                    showOnProgressBox.value = false
                                    makeSuccessWindow("PDF ($name) Created Successfully!")
                                }
                            }
                        } catch (e: Exception) {
                            showOnProgressBox.value = false
                            makeErrorWindow(e.message.toString())
                        }
                    }
                )
            }


            //successMessage
            AnimatedVisibility(showSuccessMessage.value) {
                SuccessFullDialogBox(
                    showSuccessMessage,
                    successMessage
                )
            }

            //failureMessage
            AnimatedVisibility(showErrorMessage.value) {
                WarnDialogBox(
                    showErrorMessage,
                    errorMessage
                )
            }

            //failureMessage
            AnimatedVisibility(showOnProgressBox.value) {
                DialogBox(
                    showOnProgressBox,
                    false,
                ) {
                    Box(modifier = Modifier.padding(12.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Please Wait It's making new PDF",
                        )
                    }
                }
            }

        }

    }
}


private fun getPDF(): PDFData? {
    var pdfData: PDFData? = null
    val fileChooser = JFileChooser().apply {
        fileFilter = FileNameExtensionFilter("PDf", "pdf")
        dialogTitle = "PDF Files"
    }
    val returnValue = fileChooser.showOpenDialog(null)
    if (returnValue == JFileChooser.APPROVE_OPTION) {
        var pdfDocument: PDDocument? = null
        try {
            val file = fileChooser.selectedFile
            pdfDocument = PDDocument.load(file)
            pdfData = PDFData(
                dir = file.absolutePath,
                name = file.nameWithoutExtension,
                numberOfPages = pdfDocument.numberOfPages
            )
        } catch (e: Exception) {
            println(e)
        } finally {
            pdfDocument?.close()
        }
    }
    return pdfData
}

private fun pdfRequest(
    onSuccess: (pdf: PDFData?) -> Unit,
    onFail: (error: Exception) -> Unit,
    onFinish: (pdf: PDFData?) -> Unit = {},
): PDFData? {
    var pdf: PDFData? = null
    try {
        pdf = getPDF()
        onSuccess(pdf)
    } catch (e: Exception) {
        onFail(e)
    } finally {
        onFinish(pdf)
    }
    return pdf
}


private fun createNewPdf(
    pdfName: String,
    path: String,
    pdfList: List<PDFData>,
    onError: (e: Exception) -> Unit = {},
    onSuccess: () -> Unit = {}
) {
    val suffix = ".pdf"
    var differPart = 0
    var newFile = File(path, pdfName + suffix)
    try {
        while (newFile.exists()) {
            differPart += 1
            newFile = File(path, pdfName + differPart.toString() + suffix)
        }
        PdfWriter(newFile.path).use { writer ->
            PdfDocument(writer).use { outputPDf ->
                val merger = PdfMerger(outputPDf)
                pdfList.forEach { pdfData ->
                    PdfReader(pdfData.dir).use { reader ->
                        PdfDocument(reader).use { srcPdf ->
                            val startingPage = pdfData.chosenPageRange.first()
                            val endingPage = pdfData.chosenPageRange.last()
                            merger.merge(srcPdf, startingPage, endingPage)
                        }
                    }
                }
            }
        }
        onSuccess()
    } catch (e: Exception) {
        onError(e)
    }
}

