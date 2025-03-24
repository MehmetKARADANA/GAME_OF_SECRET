package com.mobile.gameofsecret

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import com.mobile.gameofsecret.ui.screens.PreScreen
import com.mobile.gameofsecret.ui.screens.SettingScreen
import com.mobile.gameofsecret.ui.theme.GameofsecretTheme
import com.mobile.gameofsecret.viewmodels.GamerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class DestinationScreen(var route : String){
    data object Menu :DestinationScreen("menu")
    data object Pre : DestinationScreen("pre")
    data object Settings : DestinationScreen("settings")

}

class MainActivity : ComponentActivity() {
    private val gamerViewModel: GamerViewModel by viewModels()
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
                MenuScreen(navController,gamerViewModel)
            }

            composable(DestinationScreen.Settings.route) {
                SettingScreen()
            }
            composable(DestinationScreen.Pre.route) {
                PreScreen(gamerViewModel)
            }
        }

    }


    /*override fun onStop() {
        super.onStop()
        // Activity'nin görünürlükten kaldırılması (arka planda) durumunda veritabanını temizleme
        Log.d("DeleteAll", "Sekme (Activity) stop oldu, veritabanı temizleniyor...")
        gamerViewModel.deleteAll()  // Veritabanını temizleme işlemi
    }*/

}
