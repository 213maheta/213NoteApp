package com.twoonethree.noteapp.navigationsetup

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.twoonethree.noteapp.homescreen.HomeViewModel
import com.twoonethree.noteapp.addnote.AddNoteScreen
import com.twoonethree.noteapp.authentication.MobileOTPLoginScreen
import com.twoonethree.noteapp.homescreen.HomeScreen
import com.twoonethree.noteapp.model.NoteModel
import com.twoonethree.noteapp.utils.ScreenName
import com.twoonethree.noteapp.utils.fromJson
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationSetup()
{
    val navController = rememberNavController()
    val navigateTo = {route:String -> navController.navigate(route)}
    val noteViewModel: HomeViewModel = koinViewModel()

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    val startDestination =  if(currentUser != null) ScreenName.HomeScreen else ScreenName.LogInScreem

    NavHost(navController = navController, startDestination = startDestination){
        composable(route = ScreenName.HomeScreen)
        {
            HomeScreen(navigateTo, noteViewModel)
        }
        composable(
            route = "${ScreenName.AddNoteScreen}/{noteModel}",
            arguments = listOf(
                navArgument("noteModel") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val message = backStackEntry.arguments?.getString("noteModel")
            val noteModel = fromJson<NoteModel>(message)
            AddNoteScreen(noteModel = noteModel)
        }

        composable(route = ScreenName.AddNoteScreen)
        {
            AddNoteScreen(noteModel = null)
        }

        composable(route = ScreenName.LogInScreem)
        {
            MobileOTPLoginScreen(navigateTo = navigateTo)
        }
    }
}