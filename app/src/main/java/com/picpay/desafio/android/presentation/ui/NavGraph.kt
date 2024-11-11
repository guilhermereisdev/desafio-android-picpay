package com.picpay.desafio.android.presentation.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.picpay.desafio.android.presentation.ui.screen.SplashScreen
import com.picpay.desafio.android.presentation.ui.screen.UserListScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object UserListScreen : Screen("user_list_screen")
}

@Composable
fun NavGraph(navController: NavHostController) {

    val systemUiController = rememberSystemUiController()
    val useDarkIcons = isSystemInDarkTheme()

    systemUiController.setSystemBarsColor(
        color = MaterialTheme.colorScheme.primaryContainer,
        darkIcons = useDarkIcons
    )


    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }
        composable(Screen.UserListScreen.route) {
            UserListScreen(navController = navController)
        }
    }
}
