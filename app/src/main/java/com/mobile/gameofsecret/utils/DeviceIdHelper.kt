package com.mobile.gameofsecret.utils

import android.content.Context
import android.provider.Settings
import java.util.UUID

/**
 * Cihaz ID yönetimi için yardımcı sınıf
 * Kullanıcı girişi olmadan cihazı benzersiz şekilde tanımlar
 */
object DeviceIdHelper {

    private const val PREFS_NAME = "device_prefs"
    private const val KEY_DEVICE_ID = "device_id"

    /**
     * Cihazın benzersiz ID'sini döndürür
     * Önce Android ID kullanır, başarısız olursa UUID oluşturur ve saklar
     */
    fun getDeviceId(context: Context): String {
        // Önce kaydedilmiş ID'yi kontrol et
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedId = prefs.getString(KEY_DEVICE_ID, null)

        if (!savedId.isNullOrEmpty()) {
            return savedId
        }

        // Android ID'yi al
        val androidId = try {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } catch (e: Exception) {
            null
        }

        // Geçerli bir Android ID varsa kullan, yoksa UUID oluştur
        val deviceId = if (!androidId.isNullOrEmpty() && androidId != "9774d56d682e549c") {
            // "9774d56d682e549c" bazı cihazlarda dönen sabit değer
            androidId
        } else {
            UUID.randomUUID().toString()
        }

        // ID'yi kaydet
        prefs.edit().putString(KEY_DEVICE_ID, deviceId).apply()

        return deviceId
    }

    /**
     * 6 karakterlik benzersiz oyun kodu oluşturur
     * Örnek: ABC123, XYZ789
     */
    fun generateGameCode(): String {
        val chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789" // Karışıklık olmaması için bazı karakterler çıkarıldı (I, O, 0, 1)
        return (1..6)
            .map { chars.random() }
            .joinToString("")
    }

    /**
     * Benzersiz soru ID'si oluşturur
     */
    fun generateQuestionId(): String {
        return UUID.randomUUID().toString().take(12)
    }

    /**
     * Oyuncu adını doğrular
     */
    fun isValidPlayerName(name: String): Boolean {
        return name.trim().length in 2..20
    }

    /**
     * Oyun kodunu doğrular
     */
    fun isValidGameCode(code: String): Boolean {
        return code.length == 6 && code.all { it.isLetterOrDigit() }
    }
}
