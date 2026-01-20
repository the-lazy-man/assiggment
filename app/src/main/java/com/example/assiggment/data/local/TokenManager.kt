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

    companion object {
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_LOGIN_ID = "login_id"
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

    fun clear() {
        prefs.edit().clear().apply()
    }
}
