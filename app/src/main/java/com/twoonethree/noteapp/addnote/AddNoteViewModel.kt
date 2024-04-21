package com.twoonethree.noteapp.addnote

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twoonethree.noteapp.NoteRepository
import com.twoonethree.noteapp.model.NoteModel
import com.twoonethree.noteapp.roomsetup.NoteDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNoteViewModel(val noteRepository: NoteRepository):ViewModel() {

    val noteTitle = mutableStateOf("")
    val noteDescription = mutableStateOf("")
    val noteBackgroundColor = mutableStateOf(0)
    val toastMessage = mutableStateOf("")
    val showColorSheet = mutableStateOf(false)

    val onBackColorSelect = {selectedColor:Int -> noteBackgroundColor.value = selectedColor}
    val changeColorSheet = {value:Boolean -> showColorSheet.value = value}

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
                noteTitle = noteTitle.value,
                noteDescription = noteDescription.value,
                backColor = noteBackgroundColor.value
            ))
        }
        showToast("Note added successfully")
    }

    fun showToast(message:String)
    {
        toastMessage.value = message
    }



}