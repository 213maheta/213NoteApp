package com.twoonethree.noteapp.roomsetup

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.twoonethree.noteapp.model.NoteModel

@Dao
interface NoteDao {

    @Query("SELECT * FROM NoteTable")
    fun getAll(): List<NoteModel>

    @Insert
    fun add(note: NoteModel)
}