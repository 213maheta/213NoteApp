package com.twoonethree.noteapp

import com.twoonethree.noteapp.model.NoteModel
import com.twoonethree.noteapp.roomsetup.NoteDao

class NoteRepository(val noteDao: NoteDao) {

    suspend fun getAllNotes(): List<NoteModel> {
        return noteDao.getAll()
    }

    suspend fun addNote(noteModel: NoteModel)
    {
        noteDao.add(noteModel)
    }

    suspend fun deleteNote(noteList: List<NoteModel>){
        noteDao.delete(noteList)
    }

    suspend fun updateNote(noteModel: NoteModel){
        noteDao.update(noteModel)
    }

}