package com.twoonethree.noteapp.addnote

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

data class AddNoteState(
    var noteDescription:String = "",
    val noteBackColor:Color = Color.White,
    val textSize: TextUnit = 16.sp,
)
