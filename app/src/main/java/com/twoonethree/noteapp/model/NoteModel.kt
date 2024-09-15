package com.twoonethree.noteapp.model

import android.os.Parcelable
import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentSnapshot
import com.twoonethree.noteapp.utils.FireBaseString
import com.twoonethree.noteapp.utils.SyncType
import kotlinx.parcelize.Parcelize
import java.util.Objects

@Parcelize
@Entity(tableName = "NoteTable")
data class NoteModel(
    @PrimaryKey val primaryKey:Long,
    val noteTitle:String,
    val noteDescription:String,
    val importance:Int = 0,
    val backColor:Int = 0,
    var isSelected:Boolean = false,
    var isSynced:Int = SyncType.SYNCED
):Parcelable


fun List<DocumentSnapshot>.toNoteModel(): List<NoteModel> {
    val noteModelList = mutableListOf<NoteModel>()
    this.forEach{
        val noteModel = NoteModel(
            primaryKey = it.id.toLong(),
            noteTitle = it.data?.get("noteTitle") as String,
            noteDescription = it.data?.get("noteDescription") as String,
            backColor = (it.data?.get("backColor") as Long).toInt(),
            isSynced = SyncType.SYNCED,
        )
        noteModelList.add(noteModel)
    }
    return noteModelList.toList()
}

fun NoteModel.toHashMap(): HashMap<String, Any> {
    return hashMapOf(
        "noteTitle" to this.noteTitle,
        "noteDescription" to this.noteDescription,
        "importance" to this.importance,
        "backColor" to this.backColor,
    )
}

