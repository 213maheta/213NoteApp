package com.twoonethree.noteapp.profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.twoonethree.noteapp.R
import com.twoonethree.noteapp.dialog.ConfirmActionDialog
import com.twoonethree.noteapp.sealed.NoteEvent
import com.twoonethree.noteapp.showToast
import com.twoonethree.noteapp.utils.MessageBox
import com.twoonethree.noteapp.utils.ScreenName
import com.twoonethree.noteapp.utils.toDp
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProfilePage(navController: NavController, vm: ProfileViewModel = koinViewModel()) {

    val context = LocalContext.current
    val onLogOutClick = remember { { vm.isLogoutDialogShow.value = true } }
    val onDeleteClick = remember { {
        if(vm.checkInterNet())
            vm.isDeleteDialogShow.value = true
    } }
    val onSyncClick = remember { { vm.isSyncFromServer.value = true } }
    val onSyncConfirm = remember { { vm.syncFromServer() } }
    val clearNotes = remember { { vm.clearNotes() } }
    val deleteUserOnServer = remember { { vm.deleteUserOnSever() } }
    val isMessageBoxShow = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = vm.messageBox.value) {
        if (vm.messageBox.value.isEmpty())
            return@LaunchedEffect
        isMessageBoxShow.value = true
        delay(3000)
        isMessageBoxShow.value = false
    }

    LaunchedEffect(key1 = vm.noteRepository.noteEvent.value) {
        when (vm.noteRepository.noteEvent.value) {
            NoteEvent.NoInternet -> vm.messageBox.value = "Internet not available"
            NoteEvent.NoDataAvailable -> vm.messageBox.value = "No data available on server"
            NoteEvent.DataSynced -> context.showToast("Data synced sucessfully")
            NoteEvent.USER_DELETED -> {
                navController.navigate(ScreenName.LogInScreen) { popUpTo(ScreenName.HomeScreen) { inclusive = true } }
                context.showToast("User deleted sucessfully")
            }
            is NoteEvent.Failure -> {
                vm.messageBox.value = (vm.noteRepository.noteEvent.value as NoteEvent.Failure).message
            }
            NoteEvent.Empty -> Unit
            else -> Unit
        }
        vm.noteRepository.noteEvent.value = NoteEvent.Empty
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.toDp())
                .background(color = Color.Red)
        ) { com.twoonethree.noteapp.addnote.TopAppBar(navController) }

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_profile), // Replace 'image_name' with your drawable file name
                contentDescription = "Login Icon", // Replace with a proper description
                modifier = Modifier.size(600.toDp()), // Modify size as needed
            )
            Spacer(modifier = Modifier.height(16.dp))
            ProfileInfo(vm.currentUser)
            Spacer(modifier = Modifier.height(16.dp))
            ProfileActionButton(
                onLogOutClick = onLogOutClick,
                onDeleteClick = onDeleteClick,
                onSyncClick = onSyncClick
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = vm.getAppVersionName(context),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }

    if (vm.isLogoutDialogShow.value) {
        LogoutWithConfirmation(
            navController = navController,
            onDismiss = { vm.isLogoutDialogShow.value = false },
            clearNotes = clearNotes
        )
    }
    if (vm.isDeleteDialogShow.value) {
        DeleteAccountWithConfirmation(
            onDismiss = { vm.isDeleteDialogShow.value = false },
            deleteUserOnServer = deleteUserOnServer,
        )
    }
    if (vm.isSyncFromServer.value) {
        SyncWithServer(onSyncConfirm, onDismiss = { vm.isSyncFromServer.value = false })
    }
    if (isMessageBoxShow.value) {
        MessageBox(message = vm.messageBox.value)
    }
}

@Composable
fun ProfileInfo(currentUser: FirebaseUser?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .border(width = 2.dp, color = Color.Red, shape = RoundedCornerShape(10.dp))
            .clip(shape = RoundedCornerShape(10.dp))
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
                text = currentUser?.displayName ?: "", // Replace with dynamic user name
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = currentUser?.phoneNumber ?: "", // Replace with dynamic description
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ProfileActionButton(
    onLogOutClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onSyncClick: () -> Unit
) {
    //Logout
    Button(
        onClick = {
            onSyncClick()
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(50.dp)
    ) {
        Text(text = "Sync Data from Server", color = Color.White)
    }

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
fun LogoutWithConfirmation(
    navController: NavController,
    onDismiss: () -> Unit,
    clearNotes: () -> Unit
) {
    ConfirmActionDialog(
        title = "Logout",
        message = "Are you sure you want to log out?",
        onConfirm = {
            FirebaseAuth.getInstance().signOut()
            clearNotes()
            navController.navigate(ScreenName.LogInScreen) {
                popUpTo(ScreenName.HomeScreen) { inclusive = true }
            }
        },
        onDismiss = { onDismiss() }
    )
}

@Composable
fun DeleteAccountWithConfirmation(
    onDismiss: () -> Unit,
    deleteUserOnServer: () -> Unit,
) {
    ConfirmActionDialog(
        title = "Delete Account",
        message = "Are you sure you want to delete your account? This action cannot be undone.",
        onConfirm = {
            deleteUserOnServer()
        },
        onDismiss = { onDismiss() }
    )
}

@Composable
fun SyncWithServer(onSyncConfirm: () -> Unit, onDismiss: () -> Unit) {
    ConfirmActionDialog(
        title = "Sync with server",
        message = "Local changes will be lost. This action cannot be undone.",
        onConfirm = { onSyncConfirm() },
        onDismiss = { onDismiss() }
    )
}


