package com.twoonethree.noteapp.navigationsetup

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.twoonethree.noteapp.homescreen.HomeViewModel
import com.twoonethree.noteapp.addnote.AddNoteScreen
import com.twoonethree.noteapp.authentication.AuthenticationViewModel
import com.twoonethree.noteapp.authentication.LoginScreen
import com.twoonethree.noteapp.authentication.OTPScreen
import com.twoonethree.noteapp.homescreen.HomeScreen
import com.twoonethree.noteapp.model.NoteModel
import com.twoonethree.noteapp.profile.ProfilePage
import com.twoonethree.noteapp.utils.ScreenName
import com.twoonethree.noteapp.utils.fromJson
import org.koin.androidx.compose.koinViewModel

@Composable
fun NavigationSetup()
{
    val navController = rememberNavController()
    val noteViewModel: HomeViewModel = koinViewModel()
    val authenticationViewModel: AuthenticationViewModel = koinViewModel()

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    val startDestination =  if(currentUser != null) ScreenName.HomeScreen else ScreenName.LogInScreen

    NavHost(navController = navController,
        startDestination = startDestination,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(500)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(500)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(500)) }
    ){
        composable(route = ScreenName.HomeScreen)
        {
            HomeScreen(navController = navController, noteViewModel)
        }
        composable(
            route = "${ScreenName.AddNoteScreen}/{noteModel}",
            arguments = listOf(navArgument("noteModel") { type = NavType.StringType }),
        )
        { backStackEntry ->
            val message = backStackEntry.arguments?.getString("noteModel")
            val noteModel = fromJson<NoteModel>(message)
            AddNoteScreen(noteModel = noteModel, navController = navController)
        }

        composable(route = ScreenName.AddNoteScreen)
        {
            AddNoteScreen(noteModel = null, navController = navController)
        }

        composable(route = ScreenName.LogInScreen)
        {
            LoginScreen(vm = authenticationViewModel, navController= navController )
        }

        composable(route = ScreenName.OTPScreen)
        {
            OTPScreen(vm = authenticationViewModel, navController = navController)
        }

        composable(route = ScreenName.ProfileScreen)
        {
            ProfilePage(navController)
        }
    }
}