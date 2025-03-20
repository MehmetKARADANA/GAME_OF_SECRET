package com.mobile.gameofsecret

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobile.gameofsecret.ui.screens.MenuScreen
import com.mobile.gameofsecret.ui.screens.SettingScreen
import com.mobile.gameofsecret.ui.theme.GameofsecretTheme

sealed class DestinationScreen(var route : String){
    data object Menu :DestinationScreen("menu")
    data object UserAdd : DestinationScreen("user_add")
    data object Settings : DestinationScreen("settings")

}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            GameofsecretTheme {
               AppNavigation()
            }
        }
    }


    @Composable
    fun AppNavigation(){
        val navController = rememberNavController()

        NavHost(navController=navController, startDestination = DestinationScreen.Menu.route){

            composable(DestinationScreen.Menu.route) {
                MenuScreen(navController)
            }

            composable(DestinationScreen.Settings.route) {
                SettingScreen()
            }
        }

    }
}

