package com.twoonethree.noteapp.homescreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.twoonethree.noteapp.model.NoteModel
import com.twoonethree.noteapp.utils.ColorProvider
import com.twoonethree.noteapp.utils.ScreenName
import com.twoonethree.noteapp.utils.TimeUtils
import com.twoonethree.noteapp.utils.toJson

@Composable
fun HomeScreen(navigateTo: (String) -> Unit, noteViewModel: HomeViewModel) {
    LaunchedEffect(Unit) {
        noteViewModel.getAllNotes()
    }

    val onSortClick = {noteViewModel.showSortDialog.value = !noteViewModel.showSortDialog.value}

    Box(modifier = Modifier.fillMaxSize())
    {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(
                deleteNote = { noteViewModel.deleteNote() },
                isLongPress = noteViewModel.isLongPress,
                onSortClick
                )

            Box(modifier = Modifier.fillMaxSize())
            {
                NoteListView(noteViewModel.noteList, noteViewModel.isLongPress, navigateTo)
            }
        }

        FloatingActionButton(
            onClick = { navigateTo(ScreenName.AddNoteScreen) },
            shape = RectangleShape,
            containerColor = Color.Red,
            contentColor = Color.White,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
        ) {
            Icon(Icons.Filled.Add, "")
        }

        if(noteViewModel.showSortDialog.value)
        {
            BottomSheetContent(onSortClick,
                noteViewModel::sortByName,
                noteViewModel::sortByTimeDescending,
                noteViewModel::sortByTimeAscending,
                )
        }
    }
}

@Composable
fun TopBar(
    deleteNote: () -> Unit,
    isLongPress: MutableState<Boolean>,
    showSortDialog: () -> Unit
)
{
    Row(verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.08f)

    ) {

        IconButton(onClick = {showSortDialog()}) {
            Icon(
                imageVector = Icons.Default.List,
                contentDescription = "Delete",
                tint = Color.Red
            )
        }

        AnimatedVisibility(visible = isLongPress.value) {
            Row{
                IconButton(onClick = {deleteNote()}) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }

                IconButton(onClick = {isLongPress.value = false}) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Delete",
                        tint = Color.Red
                    )
                }
            }
        }
    }
}

@Composable
fun NoteListView(
    noteList: List<NoteModel>,
    onLongPress: MutableState<Boolean>,
    navigate: (String) -> Unit
) {

    if(noteList.isEmpty())
    {
        Box(modifier = Modifier.fillMaxSize())
        {
            Text(text = "No Notes", modifier = Modifier.align(Alignment.Center))
        }
        return
    }

    LazyVerticalGrid(modifier = Modifier.fillMaxSize(), columns = GridCells.Fixed(2)) {
        items(noteList, key = { it.primaryKey }) {
            NoteItem(it, onLongPress, navigate)
        }
    }
}

@Composable
fun NoteItem(noteModel: NoteModel, onLongPress: MutableState<Boolean>, navigate: (String) -> Unit) {

    Column(
        modifier = Modifier
            .padding(4.dp)
            .border(width = 2.dp, color = Color.Red, shape = RoundedCornerShape(4.dp))
            .background(color = colorResource(id = ColorProvider.colorList[noteModel.backColor]))
            .padding(4.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        onLongPress.value = !onLongPress.value
                    },
                    onPress = {

                    }
                )
            },
    )
    {
        val isSelected = remember { mutableStateOf(noteModel.isSelected) }

        AnimatedVisibility(visible = onLongPress.value) {
            Box(modifier = Modifier.fillMaxWidth())
            {
                Checkbox(
                    checked = isSelected.value,
                    onCheckedChange = { isChecked ->
                        isSelected.value = isChecked
                        noteModel.isSelected = isChecked
                    }
                , modifier = Modifier.align(Alignment.CenterEnd))
            }
        }

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

        IconButton(onClick = {navigate(ScreenName.AddNoteScreen + "/${toJson(noteModel)}")}) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Delete",
                tint = Color.Red
            )
        }

        Text(
            text = TimeUtils.convertMiliToTimeDate(noteModel.primaryKey),
            textAlign = TextAlign.Start,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    onSortClick: () -> Unit,
    onAtoZclick: () -> Unit,
    onTimeDescendingClick: () -> Unit,
    onTimeAscendingClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            onSortClick()
        },
        sheetState = sheetState
    ) {
         Column {
             BottomSheetOption(value = "AtoZ", onAtoZclick)
             BottomSheetOption(value = "Time Descending", onTimeDescendingClick)
             BottomSheetOption(value = "Time Ascending", onTimeAscendingClick)

             Spacer(modifier = Modifier
                 .fillMaxWidth()
                 .height(20.dp))
         }
    }
}

@Composable
fun BottomSheetOption(value: String, onClick: () -> Unit)
{
   Box(modifier = Modifier
       .fillMaxWidth()
       .height(50.dp)
       .padding(2.dp)
       .border(width = 2.dp, color = Color.Black)
       .clickable {
           onClick()
       }
   )
   {
       Text(text = value,
           fontSize = 14.sp, textAlign = TextAlign.Center,
           fontWeight = FontWeight.Medium,
           modifier = Modifier
               .fillMaxWidth()
               .align(Alignment.Center)
       )
   }
}

