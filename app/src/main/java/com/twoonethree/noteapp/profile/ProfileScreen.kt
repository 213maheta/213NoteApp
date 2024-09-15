package com.twoonethree.noteapp.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.twoonethree.noteapp.dialog.ConfirmActionDialog
import com.twoonethree.noteapp.utils.ScreenName
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfilePage(vm : ProfileViewModel = koinViewModel(), navigateTo: (String) -> Unit) {

    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
    ) {
        ProfileInfo(vm.currentUser)

        Spacer(modifier = Modifier.height(16.dp))

        ProfileActionButton(
            onLogOutClick = {vm.isLogoutDialogShow.value = true},
            onDeleteClick = {vm.isDeleteDialogShow.value = true }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = vm.getAppVersionName(context),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }

    if(vm.isLogoutDialogShow.value)
    {
        LogoutWithConfirmation(navigateTo, onDismiss = { vm.isLogoutDialogShow.value = false })
    }

    if(vm.isDeleteDialogShow.value)
    {
        DeleteAccountWithConfirmation(navigateTo,onDismiss = { vm.isDeleteDialogShow.value = false })
    }
}


@Composable
fun ProfileInfo(currentUser: FirebaseUser?)
{
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(width = 2.dp, color = Color.Red, shape = RoundedCornerShape(10.dp))
            .background(color = Color.LightGray)
            .padding(4.dp)
    ) {

        IconButton(onClick = {}) {
            Icon(
                modifier = Modifier.size(60.dp),
                imageVector = Icons.Default.Face,
                contentDescription = "Use Pic",
                tint = Color.Red
            )
        }

        Column {
            Text(
                text = currentUser?.displayName?:"", // Replace with dynamic user name
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = currentUser?.phoneNumber?:"", // Replace with dynamic description
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ProfileActionButton(onLogOutClick: () -> Unit, onDeleteClick: () -> Unit)
{
    //Logout
    Button(
        onClick = {
            onLogOutClick()
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(text = "Logout", color = Color.White)
    }

    //Delete Account
    Button(
        onClick = {
            onDeleteClick()
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(text = "Delete Account", color = Color.White)
    }
}

@Composable
fun LogoutWithConfirmation(navigateTo: (String) -> Unit, onDismiss: () -> Unit) {
    ConfirmActionDialog(
        title = "Logout",
        message = "Are you sure you want to log out?",
        onConfirm = {
            FirebaseAuth.getInstance().signOut()
            navigateTo(ScreenName.LogInScreen)
        },
        onDismiss = { onDismiss() }
    )
}

@Composable
fun DeleteAccountWithConfirmation(navigateTo: (String) -> Unit, onDismiss: () -> Unit) {

    ConfirmActionDialog(
        title = "Delete Account",
        message = "Are you sure you want to delete your account? This action cannot be undone.",
        onConfirm = {
            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.let { user ->
                user.delete().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        navigateTo(ScreenName.LogInScreen)
                    } else {
                        // Handle error
                    }
                }
            }
        },
        onDismiss = { onDismiss() }
    )
}


