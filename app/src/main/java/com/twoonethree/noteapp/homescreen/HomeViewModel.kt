package com.twoonethree.noteapp.homescreen

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twoonethree.noteapp.NoteRepository
import com.twoonethree.noteapp.model.NoteModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(val noteRepository: NoteRepository):ViewModel() {

    val noteList = mutableStateListOf<NoteModel>()

    fun getAllNotes(){
        viewModelScope.launch(Dispatchers.IO) {
            val tempList = noteRepository.getAllNotes()
            noteList.clear()
            noteList.addAll(tempList)
        }
    }
}