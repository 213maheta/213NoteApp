package com.twoonethree.noteapp.addnote

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twoonethree.noteapp.NoteRepository
import com.twoonethree.noteapp.model.NoteModel
import com.twoonethree.noteapp.roomsetup.NoteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNoteViewModel(val noteRepository: NoteRepository):ViewModel() {

    val noteDescription = mutableStateOf("")
    val noteBackgroundColor = mutableStateOf(Color.White)
    val noteFontSize = mutableStateOf(24f)
    val toastMessage = mutableStateOf("")

    val onBackColorSelect = {selectedColor:Color -> noteBackgroundColor.value = selectedColor}

    fun addNote()
    {
        if(noteDescription.value.isEmpty())
        {
            showToast("Note is empty")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.addNote(NoteModel(
                primaryKey = System.currentTimeMillis(),
                noteDescription = noteDescription.value
            ))
        }
        showToast("Note added successfully")
    }

    fun showToast(message:String)
    {
        toastMessage.value = message
    }

}