package com.twoonethree.noteapp.roomsetup

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.twoonethree.noteapp.model.NoteModel

@Dao
interface NoteDao {

    @Query("SELECT * FROM NoteTable")
    suspend fun getAll(): List<NoteModel>

    @Insert
    suspend fun insert(note: NoteModel)

    @Insert
    suspend fun insertAll(notes: List<NoteModel>)

    @Delete
    suspend fun delete(items: List<NoteModel>)

    @Update
    suspend fun update(note: NoteModel)

    @Query("UPDATE NoteTable SET isSynced = :isSynced WHERE primaryKey = :noteId")
    suspend fun updateSyncStatus(noteId: Long, isSynced: Int)

    @Query("SELECT * FROM NoteTable WHERE isSynced != 0")
    suspend fun getAllNotesNotSynced(): List<NoteModel>

    @Query("DELETE FROM NoteTable")
    suspend fun clearNotes()
}