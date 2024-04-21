package com.twoonethree.noteapp.homescreen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorInt
import androidx.core.graphics.toColorLong
import com.twoonethree.noteapp.R
import com.twoonethree.noteapp.model.NoteModel
import com.twoonethree.noteapp.utils.ColorProvider

@Composable
fun HomeScreen(navigateTo: (String) -> Unit, noteViewModel: HomeViewModel) {
    LaunchedEffect(Unit) {
        noteViewModel.getAllNotes()
    }


    Box(modifier = Modifier.fillMaxSize())
    {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            )
            {
                NoteListView(noteViewModel.noteList)
            }
        }

        FloatingActionButton(
            onClick = { navigateTo("AddNoteScreen") },
            shape = RectangleShape,
            containerColor = Color.Red,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
        ) {
            Icon(Icons.Filled.Add, "")
        }
    }

}

@Composable
fun NoteListView(noteList: List<NoteModel>) {
    LazyVerticalGrid(modifier = Modifier.fillMaxSize(), columns = GridCells.Fixed(2)) {
        items(noteList) {
            NoteItem(it)
        }
    }
}

@Composable
fun NoteItem(noteModel: NoteModel) {
    Log.e("TAG", "NoteItem: ${noteModel.backColor}", )
    Column(
        modifier = Modifier
            .padding(4.dp)
            .border(width = 2.dp, color = Color.Red, shape = RoundedCornerShape(4.dp))
            .background(color = colorResource(id = ColorProvider.colorList[noteModel.backColor]))
            .padding(4.dp)
    )
    {
        Text(
            text = noteModel.noteTitle,
            textAlign = TextAlign.Start,
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .fillMaxWidth()
        )

        Text(
            text = noteModel.noteDescription,
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

