package com.twoonethree.noteapp.authentication

import android.app.Activity
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.twoonethree.noteapp.R
import com.twoonethree.noteapp.dialog.CircularProgressBarExample
import com.twoonethree.noteapp.sealed.Authentication
import com.twoonethree.noteapp.showToast
import com.twoonethree.noteapp.utils.MessageBox
import com.twoonethree.noteapp.utils.ScreenName
import com.twoonethree.noteapp.utils.toDp
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel


@Composable
fun LoginScreen(
    vm: AuthenticationViewModel = koinViewModel(),
    navController: NavController
) {
    val context = LocalContext.current as Activity
    val isMessageBoxShow = remember{ mutableStateOf(false) }

    LaunchedEffect(key1 = vm.messageBox.value) {
        if(vm.messageBox.value.isEmpty())
            return@LaunchedEffect
        isMessageBoxShow.value = true
        delay(3000)
        isMessageBoxShow.value = false
    }

    LaunchedEffect(key1 = vm.authentication.value) {
        when(vm.authentication.value)
        {
            Authentication.InvalidMobileNumber -> {
                vm.messageBox.value = "Invalid Mobile Number"
            }
            Authentication.OTPSent -> {
                vm.authentication.value = Authentication.Empty
                navController.navigate(ScreenName.OTPScreen){

                }
                context.showToast("OTP sent")
            }
            else -> Unit
        }
        vm.authentication.value = Authentication.Empty
    }

    Box(
        modifier = Modifier
        .fillMaxSize()
    ) { ProfileInfoInput(vm) }

    if(vm.isProgressBarShow.value)
    {
        CircularProgressBarExample()
    }

    if(isMessageBoxShow.value)
    {
        MessageBox(message = vm.messageBox.value)
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
        Image(
            painter = painterResource(id = R.drawable.ic_login1), // Replace 'image_name' with your drawable file name
            contentDescription = "Login Icon", // Replace with a proper description
            modifier = Modifier.size(600.toDp()), // Modify size as needed
        )

        Spacer(modifier = Modifier.height(16.dp))

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





