package com.mobile.gameofsecret.ui.utils

import android.provider.Settings
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.data.Event
import com.mobile.gameofsecret.ui.screens.GameTypes
import com.mobile.gameofsecret.ui.screens.Items


fun navigateTo(navController: NavController, route : String){
    navController.navigate(route){
        popUpTo(route)
        launchSingleTop=true
    }
}

@Composable
fun ObserveErrorMessage(event: Event<String>?) {
    val context = LocalContext.current

    event?.getContentIfNotHandled()?.let { message ->
        LaunchedEffect(message) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
fun getGameTypeName(gameType: GameTypes): String {
    return when (gameType) {
        GameTypes.RANDOM -> stringResource(R.string.random)
        GameTypes.SERIAL -> stringResource(R.string.serial)
        GameTypes.SPIN -> stringResource(R.string.spin_the_wheel)
    }
}

@Composable
fun getSettingName(setting: Items) : String{
    return when (setting){
        Items.LANGUAGE -> stringResource(R.string.language)
        Items.TERMS -> stringResource(R.string.terms)
        Items.PRIVACY -> stringResource(R.string.privacy)
        Items.ABOUT_US -> stringResource(R.string.about_us)
    }
}