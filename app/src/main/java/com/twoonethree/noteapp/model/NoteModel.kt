package com.twoonethree.noteapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NoteTable")
data class NoteModel(
    @PrimaryKey val primaryKey:Long,
    val noteTitle:String,
    val noteDescription:String,
    val importance:Int = 0,
    val backColor:Int = 0
)
