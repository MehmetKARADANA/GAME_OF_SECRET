package com.mobile.gameofsecret.localization

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

object LanguageManager {
    private const val LANGUAGE_KEY = "language_key"

    fun saveLanguage(context: Context, language: String) {//şimdilik app_pref VE APP_PREF i birlşetirmedim
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(LANGUAGE_KEY, language).apply()
    }
    fun getLanguage(context: Context): String {
        val supportedLanguages = listOf("en", "tr", "fr","es","de","ru","hi","ja","ko")
        val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString(LANGUAGE_KEY, null)

        return when {
            savedLanguage != null && supportedLanguages.contains(savedLanguage) -> savedLanguage
            supportedLanguages.contains(Locale.getDefault().language) -> Locale.getDefault().language
            else -> "en"
        }
    }

    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}