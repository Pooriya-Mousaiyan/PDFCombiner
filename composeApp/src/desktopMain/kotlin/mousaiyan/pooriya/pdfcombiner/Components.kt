package mousaiyan.pooriya.pdfcombiner

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import org.jetbrains.compose.resources.painterResource
import pdfcombiner.composeapp.generated.resources.Res
import pdfcombiner.composeapp.generated.resources.ic_check
import pdfcombiner.composeapp.generated.resources.ic_new_pdf
import pdfcombiner.composeapp.generated.resources.ic_pdf
import pdfcombiner.composeapp.generated.resources.ic_warning


@Composable
fun DialogBox(
    visibilityState: MutableState<Boolean>,
    closeOnDeemArea: Boolean = true,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color(0x25010101)
            )
            .zIndex(2f)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = {
                    if (closeOnDeemArea) visibilityState.value = false
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(
                    Color.White,
                    RoundedCornerShape(8.dp)
                )
        ) {
            content()
        }
    }
}


@Composable
fun SuccessFullDialogBox(
    visibilityState: MutableState<Boolean>,
    text: String
) {
    val interactionSource = remember { MutableInteractionSource() }
    DialogBox(
        visibilityState,
        closeOnDeemArea = true,
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(0.5f)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {}
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .size(120.dp),
                painter = painterResource(Res.drawable.ic_check),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = text,
                color = Color(0xff29373d)
            )
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { visibilityState.value = false }
            ) {
                Text(
                    text = "Ok",
                    color = Color.White
                )
            }
        }
    }
}


@Composable
fun WarnDialogBox(
    visibilityState: MutableState<Boolean>,
    text: String
) {
    val interactionSource = remember { MutableInteractionSource() }
    DialogBox(
        visibilityState,
        closeOnDeemArea = false,
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(0.5f)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {}
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .size(120.dp),
                painter = painterResource(Res.drawable.ic_warning),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = text,
                color = Color(0xff29373d)
            )
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = { visibilityState.value = false },
            ) {
                Text(
                    text = "Ok",
                    color = Color.White
                )
            }
        }
    }
}


@Composable
fun PDFBox(
    pdfData: PDFData,
    onDeleteClick: () -> Unit,
) {
    val suffix = ".pdf"
    var formattedPDFName = pdfData.name.lowercase().removeSuffix(".pdf")
    if (formattedPDFName.length > 11) {
        formattedPDFName = formattedPDFName.substring(0, 11) + "...$suffix"
    } else {
        formattedPDFName += suffix
    }

    val interactionSource = remember { MutableInteractionSource() }
    val deleteIconHovered by interactionSource.collectIsHoveredAsState()
    val deleteColor by animateColorAsState(
        if (deleteIconHovered) Color(0xffff1c46) else Color.White,
        label = "Color String"
    )

    Row(
        modifier = Modifier
            .height(65.dp)
            .fillMaxWidth()
            .background(
                color = Color(0xff4287f5),
                shape = RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .clickable {

            }
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(0.5f),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                interactionSource = interactionSource,
                onClick = {
                    onDeleteClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = deleteColor
                )
            }
            Image(
                painter = painterResource(Res.drawable.ic_pdf),
                null
            )
            Text(
                text = formattedPDFName,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val regex = Regex("""[0-9]*""")
                var startingPageNumber by remember {
                    mutableStateOf(
                        pdfData.chosenPageRange.first().toString()
                    )
                }
                var endingPageNumber by remember {
                    mutableStateOf(
                        pdfData.chosenPageRange.last().toString()
                    )
                }
                //Start Text Field
                OutlinedTextField(
                    modifier = Modifier
                        .width(60.dp)
                        .onFocusEvent { focusState ->
                            if (!focusState.isFocused && startingPageNumber.isEmpty()) {
                                startingPageNumber = "1"
                                pdfData.chosenPageRange[0] = startingPageNumber.toInt()
                            }
                        },
                    colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = Color.White),
                    value = startingPageNumber,
                    onValueChange = { text ->
                        if (regex.matches(text)) {
                            if (text.isNotEmpty()) {
                                if (text.toInt() in pdfData.totalPageRange()) {
                                    startingPageNumber = text
                                    pdfData.chosenPageRange[0] = text.toInt()
                                }
                            } else {
                                startingPageNumber = text
                            }
                        }
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
                Text(
                    text = "to",
                    color = Color(0xffa8caff),
                    fontSize = 12.sp
                )
                // Ending Text Filed
                OutlinedTextField(
                    modifier = Modifier
                        .width(60.dp)
                        .onFocusEvent { focusState ->
                            if (!focusState.isFocused && endingPageNumber.isEmpty()) {
                                endingPageNumber = "1"
                                pdfData.chosenPageRange[1] = startingPageNumber.toInt()
                            }
                        },
                    colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = Color.White),
                    value = endingPageNumber,
                    onValueChange = { text ->
                        if (regex.matches(text)) {
                            if (text.isNotEmpty()) {
                                if (text.toInt() in pdfData.totalPageRange()) {
                                    endingPageNumber = text
                                    pdfData.chosenPageRange[1] = text.toInt()
                                }
                            } else {
                                endingPageNumber = text
                            }

                        }
                    },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )
                )
            }
        }
    }
}


@Composable
fun PDFContainer(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.4f)
            .fillMaxHeight()
            .background(
                color = Color(0xffb0ddff),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        content()
    }
}


@Composable
fun NewPDFGeneratorBox(
    onLocationChooseButtonClicked: (MutableState<String>) -> Unit,
    onSaveButtonClicked: (String, String) -> Unit
) {
    val savePath = remember { mutableStateOf("") }
    var saveName by remember { mutableStateOf("") }
    val fontStyle = TextStyle(
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = FontFamily.Serif
    )
    var textFieldColor by remember { mutableStateOf(Color.LightGray) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                modifier = Modifier.size(250.dp).alpha(0.5f),
                painter = painterResource(Res.drawable.ic_new_pdf),
                contentDescription = null
            )

            BasicTextField(
                modifier = Modifier
                    .onFocusEvent { focusState ->
                        textFieldColor =
                            if (focusState.isFocused) Color(0xff1c6fff) else Color.LightGray
                    },
                value = saveName,
                onValueChange = { text ->
                    saveName = text
                },
                singleLine = true,
                textStyle = fontStyle
            ) { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(0.8f).height(60.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .border(1.dp, textFieldColor, RoundedCornerShape(8.dp))
                        .padding(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.8f).padding(start = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            innerTextField()
                            if (saveName.isEmpty()) {
                                Text(
                                    text = "Name",
                                    color = Color.LightGray,
                                    style = fontStyle
                                )
                            }
                        }
                    }
                }
            }

            BasicTextField(
                modifier = Modifier
                    .onFocusEvent { focusState ->
                        textFieldColor =
                            if (focusState.isFocused) Color(0xff1c6fff) else Color.LightGray
                    },
                value = savePath.value,
                onValueChange = { text ->
                    savePath.value = text
                },
                singleLine = true,
                textStyle = fontStyle
            ) { innerTextField ->
                Row(
                    modifier = Modifier.fillMaxWidth(0.8f).height(60.dp)
                        .background(Color.White, shape = RoundedCornerShape(8.dp))
                        .border(1.dp, textFieldColor, RoundedCornerShape(8.dp))
                        .padding(2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.8f).padding(start = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box {
                            innerTextField()
                            if (savePath.value.isEmpty()) {
                                Text(
                                    text = "Save Path ...",
                                    color = Color.LightGray,
                                    style = fontStyle
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {

                        IconButton(
                            modifier = Modifier.size(36.dp),
                            onClick = {
                                onLocationChooseButtonClicked(savePath)
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null
                            )
                        }
                    }
                }
            }

            Button(
                enabled = true,
                onClick = {
                    onSaveButtonClicked(saveName, savePath.value)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xff1c6fff),
                    contentColor = Color.White
                )
            ) {
                Text("Save", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    modifier = Modifier.size(18.dp),
                    painter = painterResource(Res.drawable.ic_check),
                    contentDescription = null
                )
            }

        }
    }
}

