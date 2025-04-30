package com.mobile.gameofsecret.ui.utils

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.mobile.gameofsecret.R
import com.mobile.gameofsecret.data.Event
import com.mobile.gameofsecret.ui.screens.GameTypes


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
        GameTypes.SPIN -> stringResource(R.string.serial)
    }
}