package com.twoonethree.noteapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object TimeUtils {

    fun convertMiliToTimeDate(mili:Long): String {
        val date = Date(mili)
        val sdf = getFormat()
        return sdf.format(date)
    }

    private fun getFormat(): SimpleDateFormat {
        return SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    }
}