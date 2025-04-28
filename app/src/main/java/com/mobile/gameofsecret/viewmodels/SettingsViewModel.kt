package com.mobile.gameofsecret.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.mobile.gameofsecret.data.AppPreferences
import com.mobile.gameofsecret.utils.NotificationManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SettingsViewModel(application: Application) : BaseViewModel(application){
    private val notificationManager = NotificationManager(application)
    private val appPreferences = AppPreferences(application)

    val isFirstLaunch = appPreferences.isFirstLaunch()

    private val _isNotificationEnabled = MutableStateFlow(appPreferences.isNotificationEnabled("gos"))
    val isNotificationEnabled: StateFlow<Boolean> = _isNotificationEnabled


    init {
        if (isFirstLaunch){
            setNotificationEnabled("gos",true)
            Log.d("settingviewmodel", "Topic abone olundu")
            setFirstLaunchDone()
        }
    }
    fun setNotificationEnabled(topic: String,enabled : Boolean){
        viewModelScope.launch {
            _isNotificationEnabled.value = enabled//switchin daha hızlı tepki vermesi için
            notificationManager.setNotificationEnabled(topic,enabled)
            appPreferences.setNotificationEnabled(topic, enabled)
        }
    }

    fun setFirstLaunchDone(){
        appPreferences.setFirstLaunchDone()
    }

    fun isNotificationEnabled(topic: String) : Boolean{
        return appPreferences.isNotificationEnabled(topic)
    }

}