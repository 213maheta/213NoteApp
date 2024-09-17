package com.twoonethree.noteapp.authentication

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.twoonethree.noteapp.sealed.Authentication
import java.util.concurrent.TimeUnit

class AuthenticationViewModel:ViewModel() {


    var userName = mutableStateOf("")
    var mobileNumber = mutableStateOf("")
    var isOTPSent = mutableStateOf(false)
    private val auth by lazy { FirebaseAuth.getInstance()}

    val isProgressBarShow = mutableStateOf(false)

    val authentication = mutableStateOf<Authentication>(Authentication.Empty)

    var verificationId = ""
    var token: PhoneAuthProvider.ForceResendingToken? = null

    private val firebaseCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        override fun onVerificationCompleted(credential: PhoneAuthCredential) = Unit
        override fun onVerificationFailed(e: FirebaseException) = Unit
        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {
            isOTPSent.value = true
            this@AuthenticationViewModel.verificationId = verificationId
            this@AuthenticationViewModel.token = token
            authentication.value = Authentication.OTPSent
            isProgressBarShow.value = false
        }
    }


    fun validateMobile(): Boolean {
        return mobileNumber.value.length == 10
    }

    fun validateOTP(otp:String): Boolean {
        return otp.length == 6
    }

    fun sendOTP(activity: Activity) {
        isProgressBarShow.value = true
        if(!validateMobile())
        {
            authentication.value = Authentication.InvalidMobileNumber
            return
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91${mobileNumber.value}") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout duration
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(firebaseCallback)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun resendOTP(activity: Activity) {
        isProgressBarShow.value = true
        if(!validateMobile())
        {
            authentication.value = Authentication.InvalidMobileNumber
            return
        }

        token?.let {
            val options = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber("+91${mobileNumber.value}") // Phone number to verify
                .setTimeout(60L, TimeUnit.SECONDS) // Timeout duration
                .setActivity(activity) // Activity (for callback binding)
                .setCallbacks(firebaseCallback)
                .setForceResendingToken(it)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
        }
    }


    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUserProfile()
                } else {
                    authentication.value = Authentication.SignInFailed
                }
                isProgressBarShow.value = false
            }
    }

    private fun updateUserProfile()
    {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(userName.value)
            .build()

        val user = FirebaseAuth.getInstance().currentUser

        user?.updateProfile(profileUpdates)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authentication.value = Authentication.SignInSuccessfull
                } else {
                    authentication.value = Authentication.SignInFailed
                }
            }
    }

    fun verifyOtp(otp: String) {
        isProgressBarShow.value = true
        if(!validateOTP(otp))
        {
            authentication.value = Authentication.InvalidOTP
            isProgressBarShow.value = false
            return
        }
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        signInWithPhoneAuthCredential(credential)
    }
}

