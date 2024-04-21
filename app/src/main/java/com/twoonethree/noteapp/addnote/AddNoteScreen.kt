package com.twoonethree.noteapp.addnote

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.twoonethree.noteapp.composeutils.ColorBottomSheet
import com.twoonethree.noteapp.utils.ColorProvider
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddNoteScreen(addNoteVM: AddNoteViewModel = koinViewModel()) {

    val context = LocalContext.current

    LaunchedEffect(addNoteVM.toastMessage.value) {
        if (addNoteVM.toastMessage.value.isNotEmpty()) {
            Toast.makeText(context, addNoteVM.toastMessage.value, Toast.LENGTH_LONG).show()
            addNoteVM.showToast("")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    )
    {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.05f)
                .background(color = Color.Red)
        )
        {
            TopAppBar { addNoteVM.addNote() }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f)
                .background(color = colorResource(id = ColorProvider.colorList[addNoteVM.noteBackgroundColor.value]))

        )
        {
            NoteTextField(addNoteVM.noteDescription, addNoteVM.noteTitle)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        )
        {
            EditOptionList(addNoteVM.changeColorSheet)
        }
    }

    if (addNoteVM.showColorSheet.value) {
        ColorBottomSheet(addNoteVM.onBackColorSelect, addNoteVM.changeColorSheet)
    }
}


@Composable
fun NoteTextField(noteDescription: MutableState<String>, noteTitle: MutableState<String>) {
    Column(modifier = Modifier.fillMaxSize()) {
        TextField(
            textStyle = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
            ),
            value = noteTitle.value,
            onValueChange = { noteTitle.value = it },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            placeholder = { Text("Title", fontSize = 24.sp, fontWeight = FontWeight.Medium) },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f)
        )


        TextField(
            textStyle = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
            ),
            value = noteDescription.value,
            onValueChange = { noteDescription.value = it },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            placeholder = { Text("Note", fontSize = 18.sp, fontWeight = FontWeight.Normal) },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
        )
    }
}


@Composable
fun EditOptionList(changeColorSheet: (Boolean) -> Unit) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        item() {
            Icon(
                imageVector = Icons.Default.Face,
                contentDescription = "",
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .clickable {
                        changeColorSheet(true)
                    }
            )
        }
    }
}

@Composable
fun TopAppBar(addNote: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxSize()
    ) {
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = "",
            tint = Color.White,
            modifier = Modifier
                .padding(start = 6.dp)
                .clickable {
                    addNote()
                }
        )
    }
}
