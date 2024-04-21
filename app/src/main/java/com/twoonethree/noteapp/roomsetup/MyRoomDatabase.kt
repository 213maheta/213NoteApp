package com.twoonethree.noteapp.roomsetup

import androidx.room.Database
import androidx.room.RoomDatabase
import com.twoonethree.noteapp.model.NoteModel

@Database(entities = [NoteModel::class], version = 1)
abstract class MyRoomDatabase:RoomDatabase() {
    abstract fun noteDao(): NoteDao
}