package com.mobile.gameofsecret.viewmodels

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat

class NotificationViewModel(application: Application) : BaseViewModel(application) {

    fun openNotificationSettings(context: Context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val isPermissionGranted = ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED

                if (!isPermissionGranted) {
                    try {
                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        handleException(customMessage = "Bildirim ayarları ekranı açılamadı: ${e.localizedMessage}")
                    }
                } else {
                    handleException(customMessage = "Bildirim izni zaten verildi")
                    try {
                        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        handleException(customMessage = "Bildirim ayarları ekranı açılamadı: ${e.localizedMessage}")
                    }
                }
            } else {
                handleException(customMessage = "Android 12 ve öncesinde izin gerekmiyor")
                try {
                    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    }
                    context.startActivity(intent)
                } catch (e: Exception) {
                    handleException(customMessage = "Bildirim ayarları ekranı açılamadı: ${e.localizedMessage}")
                }
            }
        } catch (e: Exception) {
            handleException(customMessage = "Beklenmeyen bir hata oluştu: ${e.localizedMessage}")
        }
    }
}