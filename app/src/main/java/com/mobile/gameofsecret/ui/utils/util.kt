package com.mobile.gameofsecret.ui.utils

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.mobile.gameofsecret.data.Event


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