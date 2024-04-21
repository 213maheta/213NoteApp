package com.twoonethree.noteapp.model

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NoteTable")
data class NoteModel(
    @PrimaryKey
    val primaryKey:Long,
    val noteDescription:String,
    val importance:Int = 0,
    val backColor:Int = Color.Blue.hashCode()
)
