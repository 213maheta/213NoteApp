package com.twoonethree.noteapp.authentication

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.twoonethree.noteapp.dialog.CircularProgressBarExample
import com.twoonethree.noteapp.sealed.Authentication
import com.twoonethree.noteapp.showToast
import com.twoonethree.noteapp.utils.ScreenName
import org.koin.androidx.compose.koinViewModel


@Composable
fun LoginScreen(
    vm: AuthenticationViewModel = koinViewModel(),
    navigateTo: (String) -> Unit
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
            Authentication.OTPSent -> {
                navigateTo(ScreenName.OTPScreen)
                context.showToast("OTP sent")
            }
            Authentication.SignInFailed -> context.showToast("Wrong OTP")
        }
        vm.authentication.value = Authentication.Empty
    }

    Box(
        modifier = Modifier
        .fillMaxSize()
    ) {
        ProfileInfoInput(vm)
    }

    if(vm.isProgressBarShow.value)
    {
        CircularProgressBarExample()
    }
}

@Composable
fun ProfileInfoInput(vm: AuthenticationViewModel)
{
    val context = LocalContext.current as Activity

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    )
    {
        OutlinedTextField(
            value = vm.userName.value,
            onValueChange = { vm.userName.value = it },
            label = { Text("User Name") },
        )

        OutlinedTextField(
            value = vm.mobileNumber.value,
            onValueChange = {
                if (it.length <= 10) {  // Limit to 10 characters
                    vm.mobileNumber.value = it
                } },
            label = { Text("Mobile Number") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(16.dp))

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




