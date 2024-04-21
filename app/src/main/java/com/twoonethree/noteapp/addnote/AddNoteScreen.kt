package com.twoonethree.noteapp.addnote

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.twoonethree.noteapp.composeutils.ColorList
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun AddNoteScreen(addNoteVM: AddNoteViewModel = koinViewModel()) {

    val context = LocalContext.current

    LaunchedEffect(addNoteVM.toastMessage.value) {
        if(addNoteVM.toastMessage.value.isNotEmpty())
        {
            Toast.makeText(context, addNoteVM.toastMessage.value, Toast.LENGTH_LONG).show()
            addNoteVM.showToast("")
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
        )
        {
            TextField(
                textStyle = TextStyle(fontSize = addNoteVM.noteFontSize.value.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center),
                value = addNoteVM.noteDescription.value,
                onValueChange = { addNoteVM.noteDescription.value = it },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = addNoteVM.noteBackgroundColor.value,
                    unfocusedContainerColor = addNoteVM.noteBackgroundColor.value
                ),
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        Box(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
        )
        {
            ColorList(addNoteVM.onBackColorSelect)
        }

        Slider(
            value = addNoteVM.noteFontSize.value,
            onValueChange = { addNoteVM.noteFontSize.value = it },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.secondary,
                activeTrackColor = MaterialTheme.colorScheme.secondary,
                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            steps = 50,
            valueRange = 0f..50f
        )


        Box(modifier = Modifier.fillMaxSize())
        {
            Button(
                onClick = {
                    addNoteVM.addNote()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(4.dp)
                    .align(Alignment.Center)

            ) {
                Text(text = "Add Note")
            }
        }

    }
}


@Composable
fun NoteTextField()
{

}