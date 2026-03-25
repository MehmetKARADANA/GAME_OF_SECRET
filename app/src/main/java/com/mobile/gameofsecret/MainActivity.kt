package com.mobile.gameofsecret

import android.app.Application
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.mobile.gameofsecret.data.roomdb.getDatabase
import com.mobile.gameofsecret.ui.components.OnboardingDialog
import com.mobile.gameofsecret.ui.screens.AboutUsScreen
import com.mobile.gameofsecret.ui.screens.DareScreen
import com.mobile.gameofsecret.ui.screens.InfoScreen
import com.mobile.gameofsecret.ui.screens.LanguageScreen
import com.mobile.gameofsecret.ui.screens.MenuScreen
import com.mobile.gameofsecret.ui.screens.PreScreen
import com.mobile.gameofsecret.ui.screens.PrivacyScreen
import com.mobile.gameofsecret.ui.screens.RandomGameScreen
import com.mobile.gameofsecret.ui.screens.RotateSpinWheelScreen
import com.mobile.gameofsecret.ui.screens.SerialGameScreen
import com.mobile.gameofsecret.ui.screens.SettingScreen
import com.mobile.gameofsecret.ui.screens.SpinWheelScreen
import com.mobile.gameofsecret.ui.screens.TermsScreen
import com.mobile.gameofsecret.ui.screens.TruthOrDareScreen
import com.mobile.gameofsecret.ui.screens.TruthScreen
import com.mobile.gameofsecret.ui.screens.CreateGroupGameScreen
import com.mobile.gameofsecret.ui.screens.JoinGroupGameScreen
import com.mobile.gameofsecret.ui.screens.AddQuestionsScreen
import com.mobile.gameofsecret.ui.screens.WaitingRoomScreen
import com.mobile.gameofsecret.ui.screens.GroupGamePlayScreen
import com.mobile.gameofsecret.ui.theme.GameofsecretTheme
import com.mobile.gameofsecret.utils.NotificationPermissionHelper
import com.mobile.gameofsecret.viewmodels.GamerViewModel
import com.mobile.gameofsecret.viewmodels.GroupGameViewModel
import com.mobile.gameofsecret.viewmodels.NotificationViewModel
import com.mobile.gameofsecret.viewmodels.QuizViewModel
import com.mobile.gameofsecret.viewmodels.SettingsViewModel
import com.mobile.gameofsecret.viewmodels.TaskViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class DestinationScreen(var route: String) {
    data object Menu : DestinationScreen("menu/{selectedMode}") {
        fun createRoute(selectedMode: String) = "menu/$selectedMode"
    }
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
    data object RotateSpinWheel : DestinationScreen("rotate_spin")
    data object Info : DestinationScreen("info")

    // Grup oyunu ekranları
    data object CreateGroupGame : DestinationScreen("create_group_game")
    data object JoinGroupGame : DestinationScreen("join_group_game/{gameCode}") {
        fun createRoute(gameCode: String? = null) = if (gameCode != null) "join_group_game/$gameCode" else "join_group_game/none"
    }
    data object AddQuestions : DestinationScreen("add_questions/{gameCode}") {
        fun createRoute(gameCode: String) = "add_questions/$gameCode"
    }
    data object WaitingRoom : DestinationScreen("waiting_room/{gameCode}") {
        fun createRoute(gameCode: String) = "waiting_room/$gameCode"
    }
    data object GroupGamePlay : DestinationScreen("group_game_play/{gameCode}") {
        fun createRoute(gameCode: String) = "group_game_play/$gameCode"
    }
}

class GosApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val db = getDatabase(this)
        CoroutineScope(Dispatchers.IO).launch {
            db.gamerDao().deleteAll()
            db.taskDao().deleteAll()
        }
    }
}


class MainActivity : BaseActivity() {

    private lateinit var notificationPermissionHelper: NotificationPermissionHelper

    // Deep link'ten gelen oyun kodu
    private var deepLinkGameCode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        MobileAds.initialize(this)

        // Deep link kontrolü
        handleDeepLink()

        notificationPermissionHelper = NotificationPermissionHelper(this)
        notificationPermissionHelper.requestNotificationPermission()
        setContent {
            GameofsecretTheme {
                AppNavigation()
            }
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleDeepLink()
    }

    private fun handleDeepLink() {
        val data = intent?.data
        if (data != null) {
            Log.d("DeepLink", "Deep link received: $data")

            when {
                // gameofsecret://join?gameId=ABC123
                data.scheme == "gameofsecret" && data.host == "join" -> {
                    deepLinkGameCode = data.getQueryParameter("gameId")
                    Log.d("DeepLink", "Game code from custom scheme: $deepLinkGameCode")
                }
                // https://gameofsecret.app/g/ABC123
                data.host == "gameofsecret.app" && data.pathSegments.size >= 2 && data.pathSegments[0] == "g" -> {
                    deepLinkGameCode = data.pathSegments[1]
                    Log.d("DeepLink", "Game code from web link: $deepLinkGameCode")
                }
            }
        }
    }

    @Composable
    fun AppNavigation() {
        val gamerViewModel: GamerViewModel by viewModels()
        val quizViewModel: QuizViewModel by viewModels()
        val notificationViewModel: NotificationViewModel by viewModels()
        val settingsViewModel: SettingsViewModel by viewModels()
        val taskViewModel: TaskViewModel by viewModels()
        val groupGameViewModel: GroupGameViewModel by viewModels()
        val navController = rememberNavController()

        var showOnboarding by remember { mutableStateOf(true) }
        if (settingsViewModel.isFirstLaunch) {
            //initi default topic aboneliği ve first launch preferencei yönetiyor
            Log.d("settingsViewModel", "init running")
            if (showOnboarding) {
                  OnboardingDialog(
                      onDismiss = {
                          showOnboarding = false
                      })
                Log.d("main", "firstlaunch")
            }
        }
        val shouldNavigateToLanguages = intent.getBooleanExtra("navigate_to_languages", false)

        // Deep link varsa oyuna katılma ekranına yönlendir
        val startDestination = when {
            deepLinkGameCode != null -> DestinationScreen.JoinGroupGame.createRoute(deepLinkGameCode)
            shouldNavigateToLanguages -> DestinationScreen.Languages.route
            else -> DestinationScreen.Pre.route
        }

        // Deep link kullanıldıktan sonra sıfırla
        LaunchedEffect(deepLinkGameCode) {
            if (deepLinkGameCode != null) {
                deepLinkGameCode = null
            }
        }

        NavHost(navController = navController,
            startDestination = startDestination) {


            composable(DestinationScreen.Menu.route) {
                val selectedMode = it.arguments?.getString("selectedMode") ?: DestinationScreen.RandomGame.route
                MenuScreen(navController, gamerViewModel,// selectedMode,
             quizViewModel)
            }

            composable(DestinationScreen.Settings.route) {
                SettingScreen(navController, settingsViewModel, notificationViewModel)
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
                SpinWheelScreen(navController, taskViewModel)
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
            composable(DestinationScreen.RotateSpinWheel.route) {
                RotateSpinWheelScreen(taskViewModel, navController)
            }

            composable(DestinationScreen.Info.route) {
                InfoScreen()
            }

            // Grup oyunu ekranları
            composable(DestinationScreen.CreateGroupGame.route) {
                CreateGroupGameScreen(
                    navController = navController,
                    groupGameViewModel = groupGameViewModel
                )
            }

            composable(DestinationScreen.JoinGroupGame.route) {
                val gameCode = it.arguments?.getString("gameCode")
                JoinGroupGameScreen(
                    navController = navController,
                    groupGameViewModel = groupGameViewModel,
                    gameCodeFromDeepLink = if (gameCode != "none") gameCode else null
                )
            }

            composable(DestinationScreen.AddQuestions.route) {
                val gameCode = it.arguments?.getString("gameCode") ?: ""
                AddQuestionsScreen(
                    navController = navController,
                    groupGameViewModel = groupGameViewModel,
                    gameCode = gameCode
                )
            }

            composable(DestinationScreen.WaitingRoom.route) {
                val gameCode = it.arguments?.getString("gameCode") ?: ""
                WaitingRoomScreen(
                    navController = navController,
                    groupGameViewModel = groupGameViewModel,
                    gameCode = gameCode
                )
            }

            composable(DestinationScreen.GroupGamePlay.route) {
                val gameCode = it.arguments?.getString("gameCode") ?: ""
                GroupGamePlayScreen(
                    navController = navController,
                    groupGameViewModel = groupGameViewModel,
                    gameCode = gameCode
                )
            }
        }

    }


}
