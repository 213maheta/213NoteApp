package com.twoonethree.noteapp.addnote

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.twoonethree.noteapp.composeutils.ColorBottomSheet
import com.twoonethree.noteapp.model.NoteModel
import com.twoonethree.noteapp.utils.ColorProvider
import com.twoonethree.noteapp.utils.toDp
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalComposeUiApi::class)
@Composable
fun AddNoteScreen(vm : AddNoteViewModel = koinViewModel(), noteModel: NoteModel?, popup:() -> Boolean) {

    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        noteModel?.let {
            vm.updateNewModel = it
            vm.noteTitle.value = it.noteTitle
            vm.noteDescription.value = it.noteDescription
            vm.noteBackgroundColor.value = it.backColor
        }
    }

    LaunchedEffect(vm.toastMessage.value) {
        if (vm.toastMessage.value.isNotEmpty()) {
            Toast.makeText(context, vm.toastMessage.value, Toast.LENGTH_LONG).show()
            vm.showToast("")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding())
    {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.toDp())
                .background(color = Color.Red)
        ) { TopAppBar(popup) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.90f)
                .background(color = colorResource(id = ColorProvider.colorList[vm.noteBackgroundColor.value]))
        ) { NoteTextField(vm.noteDescription, vm.noteTitle) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) { BottomBar(vm.changeColorSheet, vm::addNote, popup) }
    }

    if (vm.showColorSheet.value) {
        ColorBottomSheet(vm.onBackColorSelect, vm.changeColorSheet)
    }
}


@Composable
fun NoteTextField(noteDescription: MutableState<String>, noteTitle: MutableState<String>) {

    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        delay(700)
        focusRequester.requestFocus()
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            textStyle = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            ),
            value = noteTitle.value,
            onValueChange = {
                noteTitle.value = it },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            placeholder = { Text("Title", fontSize = 24.sp, fontWeight = FontWeight.Medium) },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.toDp())
                .focusRequester(focusRequester)
        )

        TextField(
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center
            ),
            value = noteDescription.value,
            onValueChange = { noteDescription.value = it },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            placeholder = { Text("Description", fontSize = 18.sp, fontWeight = FontWeight.Normal) },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
        )
    }
}


@Composable
fun BottomBar(changeColorSheet: (Boolean) -> Unit, addNote: () -> Unit, onDicardClick: () -> Boolean) {
    Row(horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        ButtonWithIcon(Icons.Default.Close, "Discard") { onDicardClick() }
        ButtonWithIcon(Icons.Default.Face, "Color") { changeColorSheet(true) }
        ButtonWithIcon(Icons.Default.Done, "Done") { addNote() }
    }
}

@Composable
fun TopAppBar(popup: () -> Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "",
            tint = Color.White,
            modifier = Modifier
                .padding(start = 6.dp)
                .clickable {
                    popup()
                }
        )
    }
}

@Composable
fun ButtonWithIcon(imageVector: ImageVector, text: String, onClick: () -> Unit) {
    Button(
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
        Icon(
            imageVector = imageVector, // Choose an icon
            contentDescription = text, // Description for accessibility
            modifier = Modifier.size(24.dp) // Size of the icon
        )
        Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
        Text(text = text) // The button text
    }
}

