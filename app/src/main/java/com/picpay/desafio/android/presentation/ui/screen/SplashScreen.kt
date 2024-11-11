package com.picpay.desafio.android.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.picpay.desafio.android.presentation.ui.Screen
import com.picpay.desafio.android.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.picpay_logo),
            contentDescription = "Ícone de Dinheiro",
            tint = Color.White,
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center)
        )
    }
    LaunchedEffect(true) {
        delay(5000)
        navController.navigate(Screen.UserListScreen.route) {
            popUpTo(Screen.Splash.route) { inclusive = true }
        }
    }
}
