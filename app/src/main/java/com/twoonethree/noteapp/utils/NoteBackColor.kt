package com.twoonethree.noteapp.utils

sealed class NoteBackColor() {
    data object Red:NoteBackColor()
    data object Blue:NoteBackColor()
    data object Green:NoteBackColor()
    data object Yellow:NoteBackColor()
    data object Magneta:NoteBackColor()

}