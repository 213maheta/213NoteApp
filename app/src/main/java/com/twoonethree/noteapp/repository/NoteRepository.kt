package com.twoonethree.noteapp.repository

import androidx.compose.runtime.mutableStateOf
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.twoonethree.noteapp.model.NoteModel
import com.twoonethree.noteapp.model.toHashMap
import com.twoonethree.noteapp.model.toNoteModel
import com.twoonethree.noteapp.network.NetworkMonitor
import com.twoonethree.noteapp.roomsetup.NoteDao
import com.twoonethree.noteapp.sealed.NoteEvent
import com.twoonethree.noteapp.utils.FireBaseString
import com.twoonethree.noteapp.utils.SyncType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class NoteRepository(val noteDao: NoteDao, val firestore:FirebaseFirestore) {

    val noteEvent = mutableStateOf<NoteEvent>(NoteEvent.Empty)
    val scope = CoroutineScope(Dispatchers.IO)

    suspend fun syncNotesToFirestore() {
        scope.launch {
            val localNotes = noteDao.getAllNotesNotSynced()
            if(localNotes.isEmpty())
            {
                return@launch
            }
            localNotes.forEach { note ->
                when(note.isSynced)
                {
                    SyncType.ADD -> {
                        addOrUpdateNoteToFirestore(note, isUpdate = false, isSync = true)
                    }
                    SyncType.UPDATE -> {
                        addOrUpdateNoteToFirestore(note, isUpdate = false, isSync = true)
                    }
                }
            }
            val deleteNotes = localNotes.filter {
                it.isSynced == SyncType.DELETE
            }
            if(deleteNotes.isNotEmpty())
            {
                deleteNoteFromFirestore(deleteNotes, isSync = true)
            }
            noteEvent.value = NoteEvent.DataSynced
        }
    }


    suspend fun syncNotesFromFirestore() {
        val snapshot = firestore.collection(FireBaseString.NoteTable).get().await()
        if (snapshot.documents.isEmpty())
        {
            noteEvent.value = NoteEvent.NoDataAvailable
            return
        }
        noteDao.clearNotes()
        val noteList = snapshot.documents.toNoteModel()
        noteDao.insertAll(noteList)
        noteEvent.value = NoteEvent.DataSynced
    }

    suspend fun getAllNotes(): List<NoteModel> {
        return noteDao.getAll().filter {
            it.isSynced != SyncType.DELETE
        }
    }

    suspend fun addNote(noteModel: NoteModel)
    {
        when(NetworkMonitor.isNetworkAvailable)
        {
            true -> {
                addOrUpdateNoteToFirestore(noteModel, false, isSync = false)
            }
            false -> {
                noteDao.insert(noteModel.also { it.isSynced = SyncType.ADD })
                noteEvent.value = NoteEvent.NoteAdded
            }
        }
    }

    suspend fun updateNote(noteModel: NoteModel){

        when(NetworkMonitor.isNetworkAvailable)
        {
            true -> {
                addOrUpdateNoteToFirestore(noteModel, true, isSync = false)
            }
            false -> {
                noteDao.update(noteModel.also { it.isSynced = SyncType.UPDATE })
                noteEvent.value = NoteEvent.NoteUpdated
            }
        }
    }

    suspend fun deleteNote(noteList: List<NoteModel>){
        when(NetworkMonitor.isNetworkAvailable)
        {
            true -> {
                deleteNoteFromFirestore(noteList, isSync = false)
            }
            false -> {
                noteList.forEach{
                    it.isSynced = SyncType.DELETE
                }
                noteDao.delete(noteList)
                noteEvent.value = NoteEvent.NoteDeleted
            }
        }
    }

    suspend fun addOrUpdateNoteToFirestore(note: NoteModel, isUpdate:Boolean, isSync:Boolean) {
        val firestore = FirebaseFirestore.getInstance()
        val notesCollection = firestore.collection(FireBaseString.NoteTable)

        val noteDataMap = note.toHashMap()

        if(isUpdate)
        {
            updateNoteToFirebase(notesCollection, note, noteDataMap, isSync)
            return
        }
        addNoteToFirebase(notesCollection, note, noteDataMap, isSync)
    }

    suspend fun updateNoteToFirebase(
        notesCollection: CollectionReference,
        note: NoteModel,
        noteDataMap: HashMap<String, Any>,
        isSync: Boolean
    )
    {
        notesCollection.document(note.primaryKey.toString()).update(noteDataMap as Map<String, Any>)
            .addOnSuccessListener {
                scope.launch {
                    noteDao.update(note.also { it.isSynced = SyncType.SYNCED })
                    if(isSync)
                    {
                        noteDao.updateSyncStatus(note.primaryKey, isSynced = SyncType.SYNCED)
                    }
                    else
                    {
                        noteDao.update(note)
                    }
                    noteEvent.value = NoteEvent.NoteUpdated
                }
            }
            .addOnFailureListener { e ->
                noteEvent.value = NoteEvent.Failure("Update error ${e.message}")
            }
    }

    suspend fun addNoteToFirebase(
        notesCollection: CollectionReference,
        note: NoteModel,
        noteDataMap: HashMap<String, Any>,
        isSync: Boolean
    )
    {
        notesCollection.document(note.primaryKey.toString()).set(noteDataMap)
            .addOnSuccessListener {
                scope.launch {
                    if(isSync){
                        noteDao.updateSyncStatus(note.primaryKey, isSynced = SyncType.SYNCED)
                    }
                    else{
                        noteDao.insert(note.also { it.isSynced = SyncType.SYNCED })
                        noteEvent.value = NoteEvent.NoteAdded
                    }
                }
            }
            .addOnFailureListener { e ->
                noteEvent.value = NoteEvent.Failure("Add error ${e.message}")
            }
    }

    suspend fun deleteNoteFromFirestore(notes: List<NoteModel>, isSync: Boolean) {
        val firestore = FirebaseFirestore.getInstance()
        val notesCollection = firestore.collection(FireBaseString.NoteTable)
        for(note in notes)
        {
            notesCollection.document(note.primaryKey.toString()).delete()
                .addOnSuccessListener {
                    scope.launch {
                        noteDao.delete(notes)
                        noteEvent.value = NoteEvent.NoteDeleted
                    }

                }
                .addOnFailureListener { e ->
                    noteEvent.value = NoteEvent.Failure("Delete error ${e.message}")
                }
        }
    }

}