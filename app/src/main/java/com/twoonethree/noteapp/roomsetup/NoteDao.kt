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
    fun getAll(): List<NoteModel>

    @Insert
    fun add(note: NoteModel)

    @Delete
    fun delete(items: List<NoteModel>)

    @Update
    fun update(note: NoteModel)
}