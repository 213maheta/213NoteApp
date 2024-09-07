package com.twoonethree.noteapp.authentication

import android.app.Activity
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.twoonethree.noteapp.sealed.Authentication
import com.twoonethree.noteapp.showToast
import com.twoonethree.noteapp.utils.ScreenName
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import kotlin.reflect.KFunction1

@Composable
fun MobileOTPLoginScreen(vm : AuthenticationViewModel = koinViewModel(), navigateTo: (String) -> Unit, ) {
    Box(
        modifier = Modifier
        .fillMaxSize()
    ) {
        val context = LocalContext.current as Activity

        LaunchedEffect(key1 = vm.authentication.value) {
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

        AnimatedVisibility(visible = !vm.isOTPSent.value) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxSize()
            )
            {
                OutlinedTextField(
                    value = vm.mobileNumber.value,
                    onValueChange = {
                        if (it.length <= 10) {  // Limit to 10 characters
                            vm.mobileNumber.value = it
                        } },
                    label = { Text("Mobile Number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )

                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red, // Custom background color
                        contentColor = Color.White // Text color
                    ),
                    onClick = {
                        vm.sendOTP(context as Activity)
                    })
                {
                    Text("Send OTP")
                }
            }
        }

        AnimatedVisibility(visible = vm.isOTPSent.value) {
            OTPInputScreen(vm::verifyOtp, vm::resendOTP)
        }
    }
}

@Composable
fun OTPInputScreen(verifyOtp: KFunction1<String, Unit>, resendOtp: KFunction1<Activity, Unit>) {
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

    var canResend by remember { mutableStateOf(true) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        if (!canResend) {
            delay(60) // Disable for 1 minute
            canResend = true
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
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

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red, // Custom background color
                contentColor = Color.White // Text color
            ),
            onClick = {
                if (canResend) {
                    resendOtp(context as Activity)
                    canResend = false
                }
            })
        {
            Text("Resend OTP")
        }
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
            Log.e("TAG", "OtpBox: $it", )
            if(it.length < 2)
            {
                otpDigit.value = it
            }
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


