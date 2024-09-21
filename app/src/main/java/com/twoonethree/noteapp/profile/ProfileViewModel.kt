package com.twoonethree.noteapp.profile

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.twoonethree.noteapp.network.NetworkMonitor
import com.twoonethree.noteapp.repository.NoteRepository
import com.twoonethree.noteapp.sealed.NoteEvent
import com.twoonethree.noteapp.utils.ScreenName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(val noteRepository: NoteRepository):ViewModel() {

    val isLogoutDialogShow = mutableStateOf(false)
    val isDeleteDialogShow = mutableStateOf(false)
    val isSyncFromServer = mutableStateOf(false)
    val messageBox = mutableStateOf("")

    val currentUser =  FirebaseAuth.getInstance().currentUser

    fun getAppVersionName(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            "Unknown"
        }
    }

    fun clearNotes()
    {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.deleteAllNotes()
        }
    }

    fun checkInterNet(): Boolean {
        if(!NetworkMonitor.isNetworkAvailable)
        {
            noteRepository.noteEvent.value = NoteEvent.NoInternet
            return false
        }
        return true
    }

    fun deleteUserOnSever()
    {
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.deleteUserData()
        }
    }

    fun syncFromServer()
    {
        if(!NetworkMonitor.isNetworkAvailable)
        {
            noteRepository.noteEvent.value = NoteEvent.NoInternet
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            noteRepository.syncNotesFromFirestore()
        }
    }
}