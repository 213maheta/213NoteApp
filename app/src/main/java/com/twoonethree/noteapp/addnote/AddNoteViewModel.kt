package com.twoonethree.noteapp.addnote

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twoonethree.noteapp.repository.NoteRepository
import com.twoonethree.noteapp.model.NoteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNoteViewModel(val noteRepository: NoteRepository):ViewModel() {

    val noteTitle = mutableStateOf("")
    val noteDescription = mutableStateOf("")
    val noteBackgroundColor = mutableStateOf(0)
    val toastMessage = mutableStateOf("")
    val showColorSheet = mutableStateOf(false)

    var updateNewModel:NoteModel? = null

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
            updateNewModel?.let {
                noteRepository.updateNote(it.copy(
                    noteTitle = noteTitle.value,
                    noteDescription = noteDescription.value,
                    backColor = noteBackgroundColor.value
                ))
            }?:run {
                noteRepository.addNote(NoteModel(
                    primaryKey = System.currentTimeMillis(),
                    noteTitle = noteTitle.value,
                    noteDescription = noteDescription.value,
                    backColor = noteBackgroundColor.value
                ))
            }
        }
        showToast("Note added successfully")
    }

    fun showToast(message:String)
    {
        toastMessage.value = message
    }
}