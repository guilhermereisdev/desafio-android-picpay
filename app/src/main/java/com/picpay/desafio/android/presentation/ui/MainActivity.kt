package com.picpay.desafio.android.presentation.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.picpay.desafio.android.presentation.ui.theme.DesafioAndroidPicPayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DesafioAndroidPicPayTheme {
                val navController = rememberNavController()
                NavGraph(navController)
            }
        }
    }
}
