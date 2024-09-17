package com.twoonethree.noteapp.authentication

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.twoonethree.noteapp.R
import com.twoonethree.noteapp.dialog.CircularProgressBarExample
import com.twoonethree.noteapp.sealed.Authentication
import com.twoonethree.noteapp.showToast
import com.twoonethree.noteapp.utils.ScreenName
import com.twoonethree.noteapp.utils.toDp
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import kotlin.reflect.KFunction1


@Composable
fun OTPScreen(
    vm: AuthenticationViewModel = koinViewModel(),
    navigateTo: (String) -> Unit
)
{
    val context = LocalContext.current as Activity

    LaunchedEffect(key1 = vm.authentication.value) {
        Log.e("TAG", "OTPScreen: ${vm.mobileNumber.value}", )
        when(vm.authentication.value)
        {
            Authentication.Empty -> Unit
            Authentication.SignInSuccessfull -> {
                navigateTo(ScreenName.HomeScreen)
                context.showToast("Signin Successfull")
            }
            Authentication.InvalidMobileNumber -> context.showToast("Invalid Mobile Number")
            Authentication.InvalidOTP -> context.showToast("Invalid OTP")
            Authentication.OTPSent -> context.showToast("OTP sent")
            Authentication.SignInFailed -> context.showToast("Wrong OTP")
        }
        vm.authentication.value = Authentication.Empty
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_otp), // Replace 'image_name' with your drawable file name
            contentDescription = "Login Icon", // Replace with a proper description
            modifier = Modifier.size(600.toDp()), // Modify size as needed
        )
        Spacer(modifier = Modifier.height(16.toDp()))
        OTPInputScreen(vm::verifyOtp, vm::resendOTP)
    }

    if(vm.isProgressBarShow.value)
    {
        CircularProgressBarExample()
    }
}


@Composable
fun OTPInputScreen(
    verifyOtp: KFunction1<String, Unit>,
    resendOtp: KFunction1<Activity, Unit>,
) {
    val otp1 = remember { mutableStateOf("") }
    val otp2 = remember { mutableStateOf("") }
    val otp3 = remember { mutableStateOf("") }
    val otp4 = remember { mutableStateOf("") }
    val otp5 = remember { mutableStateOf("") }
    val otp6 = remember { mutableStateOf("") }

    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val focusRequester4 = remember { FocusRequester() }
    val focusRequester5 = remember { FocusRequester() }
    val focusRequester6 = remember { FocusRequester() }

    LaunchedEffect(key1 = Unit) {
        focusRequester1.requestFocus()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Row (
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            OtpBox(otp1, focusRequester1, focusRequester2, focusRequester1)
            OtpBox(otp2, focusRequester2, focusRequester3, focusRequester1)
            OtpBox(otp3, focusRequester3, focusRequester4, focusRequester2)
            OtpBox(otp4, focusRequester4, focusRequester5, focusRequester3)
            OtpBox(otp5, focusRequester5, focusRequester6, focusRequester4)
            OtpBox(otp6, focusRequester6, focusRequester6, focusRequester5)
        }

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red, // Custom background color
                contentColor = Color.White // Text color
            ),
            onClick = {
                verifyOtp("${otp1.value}${otp2.value}${otp3.value}${otp4.value}${otp5.value}${otp6.value}")
            })
        {
            Text("Submit")
        }

        ResendOTP(resendOtp)
    }
}

@Composable
fun OtpBox(
    otpDigit: MutableState<String>,
    currentFocus: FocusRequester,
    nextFocus: FocusRequester,
    preFocus: FocusRequester
)
{
    OutlinedTextField(
        value = otpDigit.value,
        onValueChange = {
            otpDigit.value = it.take(1)
            if (it.length == 1) {
                nextFocus.requestFocus()
            } else if (it.isEmpty() or it.isBlank()) {
                preFocus.requestFocus()
            }
        },
        modifier = Modifier
            .focusRequester(currentFocus)
            .size(60.dp)
            .padding(4.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        maxLines = 1
    )
}

@Composable
fun ResendOTP(resendOtp: (Activity) -> Unit)
{
    val context = LocalContext.current as Activity

    var timeLeft by remember { mutableStateOf(60) }
    var isButtonEnabled by remember { mutableStateOf(false) }

    // LaunchedEffect to start the timer
    LaunchedEffect(timeLeft) {
        if (timeLeft > 0) {
            delay(1000L) // Delay for 1 second
            timeLeft -= 1
        } else {
            isButtonEnabled = true
        }
    }

    // UI components
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (timeLeft > 0) "Resend OTP in $timeLeft seconds" else "You can resend OTP now!",
            modifier = Modifier.padding(16.dp)
        )

        // Resend OTP Button
        Button(
            onClick = {
                resendOtp(context)
                timeLeft = 60 // Reset timer
                isButtonEnabled = false
            },
            enabled = isButtonEnabled // Disable the button until the timer finishes
        ) {
            Text(text = "Resend OTP")
        }
    }
}