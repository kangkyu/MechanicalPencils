package com.lininglink.mechanicalpencils.data.local

import android.content.Context
import android.content.SharedPreferences

class TokenManager(context: Context) {

    companion object {
        private const val PREFS_NAME = "auth_prefs_encrypted"
        private const val KEY_TOKEN = "auth_token"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, EncryptionHelper.encrypt(token)).apply()
    }

    fun getToken(): String? {
        val encrypted = prefs.getString(KEY_TOKEN, null) ?: return null
        return try {
            EncryptionHelper.decrypt(encrypted)
        } catch (_: Exception) {
            clearToken()
            null
        }
    }

    fun clearToken() {
        prefs.edit().remove(KEY_TOKEN).apply()
    }
}
