package com.twoonethree.noteapp.homescreen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.twoonethree.noteapp.MyApplication
import com.twoonethree.noteapp.repository.NoteRepository
import com.twoonethree.noteapp.model.NoteModel
import com.twoonethree.noteapp.network.NetworkMonitor
import com.twoonethree.noteapp.sealed.NoteEvent
import com.twoonethree.noteapp.utils.SyncType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeViewModel(val noteRepository: NoteRepository):ViewModel(), KoinComponent {

    val noteList = mutableStateListOf<NoteModel>()
    val isLongPress = mutableStateOf(false)

    val showSortDialog = mutableStateOf(false)
    val isDeleteDialogShow = mutableStateOf(false)

    val isProgressBarShow = mutableStateOf(false)
    val unSyncedData = mutableStateOf(false)

    val messageBox = mutableStateOf("")

    fun getAllNotes(){
        viewModelScope.launch(Dispatchers.IO) {
            isProgressBarShow.value = true
            val tempList = noteRepository.getAllNotes()
            noteList.clear()
            noteList.addAll(tempList)
            isProgressBarShow.value = false
            unSyncedData.value = tempList.any { it.isSynced != SyncType.SYNCED }
        }
    }

    fun deleteNote(){
        viewModelScope.launch(Dispatchers.IO) {
            isProgressBarShow.value = true
            val (selectedList, unSelectedList) = noteList.partition{
                it.isSelected
            }
            noteRepository.deleteNote(selectedList)
            noteList.clear()
            noteList.addAll(unSelectedList)
            isLongPress.value = false
            isProgressBarShow.value = false
        }
    }

    fun syncNotesToFirestore()
    {
        if(!NetworkMonitor.isNetworkAvailable)
        {
            noteRepository.noteEvent.value = NoteEvent.NoInternet
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            isProgressBarShow.value = true
            noteRepository.syncNotesToFirestore()
            noteList.forEach {
                it.isSynced = 0
            }
            unSyncedData.value = false
            isProgressBarShow.value = false
        }
    }

    fun sortByName()
    {
        val sortedList = noteList.sortedBy {
            it.noteTitle
        }

        noteList.clear()
        noteList.addAll(sortedList)
    }

    fun sortByTimeDescending()
    {
        val sortedList = noteList.sortedByDescending {
            it.noteTitle
        }

        noteList.clear()
        noteList.addAll(sortedList)
    }

    fun sortByTimeAscending()
    {
        val sortedList = noteList.sortedBy {
            it.noteTitle
        }

        noteList.clear()
        noteList.addAll(sortedList)
    }

}