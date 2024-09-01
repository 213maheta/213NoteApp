package com.twoonethree.noteapp.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "NoteTable")
data class NoteModel(
    @PrimaryKey val primaryKey:Long,
    val noteTitle:String,
    val noteDescription:String,
    val importance:Int = 0,
    val backColor:Int = 0,
    var isSelected:Boolean = false
):Parcelable

