package com.twoonethree.noteapp.profile

import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.twoonethree.noteapp.utils.ScreenName

class ProfileViewModel:ViewModel() {

    val isLogoutDialogShow = mutableStateOf(false)
    val isDeleteDialogShow = mutableStateOf(false)

    val currentUser by lazy{
        FirebaseAuth.getInstance().currentUser
    }

    fun getAppVersionName(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            "Unknown"
        }
    }

    fun logOut(){
        FirebaseAuth.getInstance().signOut() // Logs the user out
        //navigateTo(ScreenName.LogInScreen)
    }

    fun deleteAccount()
    {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        currentUser?.let { user ->
            user.delete()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                    } else {

                    }
                }
                .addOnFailureListener {

                }
        } ?: run {

        }
    }
}