package com.twoonethree.noteapp.koinsetup

import androidx.room.Room
import com.google.firebase.firestore.FirebaseFirestore
import com.twoonethree.noteapp.repository.NoteRepository
import com.twoonethree.noteapp.homescreen.HomeViewModel
import com.twoonethree.noteapp.addnote.AddNoteViewModel
import com.twoonethree.noteapp.authentication.AuthenticationViewModel
import com.twoonethree.noteapp.network.NetworkMonitor
import com.twoonethree.noteapp.profile.ProfileViewModel
import com.twoonethree.noteapp.roomsetup.MyRoomDatabase
import com.twoonethree.noteapp.utils.FireBaseString
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            MyRoomDatabase::class.java,
            FireBaseString.NoteDatabase,
        ).build()
    }

    single {
        val roomDatabase = get<MyRoomDatabase>()
        roomDatabase.noteDao()
    }

    single {
        FirebaseFirestore.getInstance()
    }

    single {
        NoteRepository(get(), get())
    }

    single {
        NetworkMonitor(get())
    }

    viewModel{ HomeViewModel(get()) }
    viewModel{ AddNoteViewModel(get()) }
    viewModel{ AuthenticationViewModel(get()) }
    viewModel{ ProfileViewModel(get()) }
}