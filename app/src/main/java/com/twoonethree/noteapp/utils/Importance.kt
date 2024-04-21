package com.twoonethree.noteapp.utils

sealed class Importance() {
    object Higher:Importance()
    object Medium:Importance()
    object Lower:Importance()
}