package com.twoonethree.noteapp.koinsetup

import androidx.room.Room
import com.twoonethree.noteapp.NoteRepository
import com.twoonethree.noteapp.homescreen.HomeViewModel
import com.twoonethree.noteapp.addnote.AddNoteViewModel
import com.twoonethree.noteapp.roomsetup.MyRoomDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            MyRoomDatabase::class.java,
            "my_database"
        ).build()
    }

    single {
        val roomDatabase = get<MyRoomDatabase>()
        roomDatabase.noteDao()
    }

    single {
        NoteRepository(get())
    }

    viewModel{ HomeViewModel(get()) }
    viewModel{ AddNoteViewModel(get()) }
}