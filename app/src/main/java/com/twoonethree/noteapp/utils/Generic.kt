package com.twoonethree.noteapp.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

inline fun <reified T> toJson(data: T): String {
    val gson = Gson()
    return gson.toJson(data)
}

inline fun <reified T> fromJson(json: String?): T? {
    val gson = Gson()
    val type = object : TypeToken<T>() {}.type
    return gson.fromJson(json, type)
}