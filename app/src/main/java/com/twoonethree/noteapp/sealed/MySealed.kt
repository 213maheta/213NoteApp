package com.twoonethree.noteapp.sealed

sealed class Authentication(){
    object SignInSuccessfull:Authentication()
    object SignInFailed:Authentication()
    object OTPSent:Authentication()
    object InvalidMobileNumber:Authentication()
    object InvalidOTP:Authentication()
    object Empty:Authentication()
}

sealed class NoteEvent(){
    object NoteAdded:NoteEvent()
    object NoteUpdated:NoteEvent()
    object NoteDeleted:NoteEvent()
    object DataSynced:NoteEvent()
    object NoInternet:NoteEvent()
    object NoDataAvailable:NoteEvent()
    object Empty:NoteEvent()
    data class Failure(val message:String):NoteEvent()
}