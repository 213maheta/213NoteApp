package com.twoonethree.noteapp.navigationsetup

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.twoonethree.noteapp.homescreen.HomeViewModel
import com.twoonethree.noteapp.addnote.AddNoteScreen
import com.twoonethree.noteapp.homescreen.HomeScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationSetup()
{
    val navController = rememberNavController()
    val navigateTo = {route:String -> navController.navigate(route)}
    val noteViewModel: HomeViewModel = koinViewModel()
    NavHost(navController = navController, startDestination = "HomeScreen"){
        composable(route = "HomeScreen")
        {
            HomeScreen(navigateTo, noteViewModel)
        }

        composable(route = "AddNoteScreen")
        {
            AddNoteScreen()
        }
    }
}