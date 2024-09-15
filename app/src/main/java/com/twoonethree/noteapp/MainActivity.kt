package com.twoonethree.noteapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.twoonethree.noteapp.navigationsetup.NavigationSetup
import com.twoonethree.noteapp.network.NetworkMonitor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val networkMonitor: NetworkMonitor by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkMonitor.registerNetworkStatus()
        setContent {
            NavigationSetup()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkMonitor.unRegisterNetworkStatus()
    }
}


fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

