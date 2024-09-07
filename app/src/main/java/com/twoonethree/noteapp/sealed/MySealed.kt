package com.twoonethree.noteapp.sealed

sealed class Authentication(){
    object SignInSuccessfull:Authentication()
    object SignInFailed:Authentication()
    object OTPSent:Authentication()
    object InvalidMobileNumber:Authentication()
    object InvalidOTP:Authentication()
    object Empty:Authentication()
}