package com.mobile.gameofsecret

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobile.gameofsecret.ui.screens.AboutUsScreen
import com.mobile.gameofsecret.ui.screens.DareScreen
import com.mobile.gameofsecret.ui.screens.LanguageScreen
import com.mobile.gameofsecret.ui.screens.MenuScreen
import com.mobile.gameofsecret.ui.screens.PreScreen
import com.mobile.gameofsecret.ui.screens.PrivacyScreen
import com.mobile.gameofsecret.ui.screens.RandomGameScreen
import com.mobile.gameofsecret.ui.screens.RotateSpinWheelScreen
import com.mobile.gameofsecret.ui.screens.SerialGameScreen
import com.mobile.gameofsecret.ui.screens.SettingScreen
import com.mobile.gameofsecret.ui.screens.SpinBottleScreen
import com.mobile.gameofsecret.ui.screens.SpinWheelScreen
import com.mobile.gameofsecret.ui.screens.TermsScreen
import com.mobile.gameofsecret.ui.screens.TruthOrDareScreen
import com.mobile.gameofsecret.ui.screens.TruthScreen
import com.mobile.gameofsecret.ui.theme.GameofsecretTheme
import com.mobile.gameofsecret.utils.NotificationPermissionHelper
import com.mobile.gameofsecret.viewmodels.GamerViewModel
import com.mobile.gameofsecret.viewmodels.NotificationViewModel
import com.mobile.gameofsecret.viewmodels.QuizViewModel
import com.mobile.gameofsecret.viewmodels.SettingsViewModel
import com.mobile.gameofsecret.viewmodels.TaskViewModel

sealed class DestinationScreen(var route: String) {
    data object Menu : DestinationScreen("menu")
    data object Pre : DestinationScreen("pre")
    data object Settings : DestinationScreen("settings")
    data object SerialGame : DestinationScreen("serial")
    data object RandomGame : DestinationScreen("random")
    data object SpinWheel : DestinationScreen("spin_wheel")
    data object Languages : DestinationScreen("languages")
    data object TruthOrDare : DestinationScreen("truth_or_dare/{name}") {
        fun createRoute(name: String) = "truth_or_dare/$name"
    }

    data object Truth : DestinationScreen("truth/{name}/{fromScreen}") {
        fun createRoute(name: String, fromScreen: String) = "truth/$name/$fromScreen"
    }

    data object Dare : DestinationScreen("dare/{name}/{fromScreen}") {
        fun createRoute(name: String, fromScreen: String) = "dare/$name/$fromScreen"
    }
    data object Privacy : DestinationScreen("privacy")
    data object Terms : DestinationScreen("terms")
    data object AboutUs : DestinationScreen("about")
    data object rotateSpinWheel : DestinationScreen("rotate_spin")

}

class MainActivity : BaseActivity() {

    private lateinit var notificationPermissionHelper : NotificationPermissionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        notificationPermissionHelper = NotificationPermissionHelper(this)
        notificationPermissionHelper.requestNotificationPermission()
        setContent {
            GameofsecretTheme {
                AppNavigation()
            }
        }
    }


    @Composable
    fun AppNavigation() {
        val gamerViewModel: GamerViewModel by viewModels()
        val quizViewModel: QuizViewModel by viewModels()
        val notificationViewModel: NotificationViewModel by viewModels()
        val settingsViewModel: SettingsViewModel by viewModels()
        val taskViewModel : TaskViewModel by viewModels()
        val navController = rememberNavController()

        var showOnboarding by remember { mutableStateOf(true) }

        if (settingsViewModel.isFirstLaunch) {
            //init default topic aboneliği ve first launch preferencei yönetiyor
            Log.d("settingsViewModel", "init running")
            if (showOnboarding) {
              /*  OnboardingDialog(
                    onDismiss = {
                        showOnboarding = false
                    })*/
                Log.d("main","firstlaunch")
            }
        }


        NavHost(navController = navController, startDestination = DestinationScreen.Menu.route) {
            composable(DestinationScreen.Menu.route) {
                MenuScreen(navController, gamerViewModel)
            }

            composable(DestinationScreen.Settings.route) {
                SettingScreen(navController,settingsViewModel,notificationViewModel)
            }
            composable(DestinationScreen.Pre.route) {
                PreScreen(gamerViewModel, navController = navController, quizViewModel)
            }

            composable(DestinationScreen.SerialGame.route) {
                SerialGameScreen(
                    gamerViewModel = gamerViewModel,
                    quizViewModel = quizViewModel,
                    navController = navController
                )
            }
            composable(DestinationScreen.RandomGame.route) {
                RandomGameScreen(gamerViewModel, navController)
            }

            composable(DestinationScreen.SpinWheel.route) {
                SpinWheelScreen(navController,taskViewModel)
            }

            composable(DestinationScreen.TruthOrDare.route) {
                val name = it.arguments?.getString("name")

                name?.let { gamer ->
                    TruthOrDareScreen(gamer, navController, quizViewModel)
                }
            }

            composable(DestinationScreen.Truth.route) {
                val name = it.arguments?.getString("name")
                //random or serial
                val fromScreen =
                    it.arguments?.getString("fromScreen") ?: DestinationScreen.RandomGame.route
                name?.let { gamer ->
                    TruthScreen(gamer, navController, quizViewModel, fromScreen)
                }
            }

            composable(DestinationScreen.Dare.route) {
                val name = it.arguments?.getString("name")
                val fromScreen =
                    it.arguments?.getString("fromScreen") ?: DestinationScreen.RandomGame.route

                name?.let { gamer ->
                    DareScreen(gamer, navController, quizViewModel, fromScreen)
                }
            }
            composable(DestinationScreen.Languages.route) {
               LanguageScreen(navController)
            }
            composable(DestinationScreen.Terms.route) {
                TermsScreen(navController)
            }
            composable(DestinationScreen.Privacy.route) {
                PrivacyScreen(navController)
            }
            composable(DestinationScreen.AboutUs.route) {
                AboutUsScreen(navController)
            }
            composable(DestinationScreen.rotateSpinWheel.route) {
                RotateSpinWheelScreen(taskViewModel,navController)
            }
        }

    }


}
