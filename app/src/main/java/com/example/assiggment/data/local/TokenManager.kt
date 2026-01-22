package com.example.assiggment.data.local

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs: SharedPreferences = context.getSharedPreferences("peanut_prefs", Context.MODE_PRIVATE)

    fun clear() {
        prefs.edit().remove(KEY_TOKEN).apply()
        // Do NOT remove saved login/password on logout (clear usually implies logout)
    }

    // Saved Credentials (Persist across logouts)
    fun saveCredentials(loginId: Int, password: String) {
        prefs.edit()
            .putInt(KEY_SAVED_LOGIN, loginId)
            .putString(KEY_SAVED_PASSWORD, password)
            .apply()
    }

    fun getSavedLoginId(): Int {
        return prefs.getInt(KEY_SAVED_LOGIN, -1)
    }

    fun getSavedPassword(): String? {
        return prefs.getString(KEY_SAVED_PASSWORD, null)
    }

    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_LOGIN_ID = "login_id"
        private const val KEY_SAVED_LOGIN = "saved_login_id"
        private const val KEY_SAVED_PASSWORD = "saved_password"
    }

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun saveLoginId(loginId: Int) {
        prefs.edit().putInt(KEY_LOGIN_ID, loginId).apply()
    }

    fun getLoginId(): Int {
        return prefs.getInt(KEY_LOGIN_ID, -1)
    }
}
