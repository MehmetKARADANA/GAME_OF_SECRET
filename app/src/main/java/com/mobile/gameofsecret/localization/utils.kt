package com.mobile.gameofsecret.localization

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.navigation.compose.rememberNavController
import com.mobile.gameofsecret.MainActivity
import com.mobile.gameofsecret.ui.utils.navigateTo

fun changeLocale(context: Context, language: String) {
    LanguageManager.saveLanguage(context, language)
    LanguageManager.setLocale(context, language)
    restartApp(context)
}

fun restartApp(context: Context) {
    val intent = Intent(context, MainActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
    context.startActivity(intent)
    if (context is Activity) {
        context.finish()
    }
}