package com.example.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "ava_studio_auth_session"
        private const val KEY_ACTIVE_USER_ID = "active_user_id"
        private const val KEY_SESSION_TOKEN = "session_token"
        private const val KEY_SESSION_TIMESTAMP = "session_timestamp"
    }

    fun saveSession(userId: String, token: String) {
        prefs.edit()
            .putString(KEY_ACTIVE_USER_ID, userId)
            .putString(KEY_SESSION_TOKEN, token)
            .putLong(KEY_SESSION_TIMESTAMP, System.currentTimeMillis())
            .apply()
    }

    fun getActiveUserId(): String? {
        return prefs.getString(KEY_ACTIVE_USER_ID, null)
    }

    fun getSessionToken(): String? {
        return prefs.getString(KEY_SESSION_TOKEN, null)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun isLoggedIn(): Boolean {
        return !getActiveUserId().isNullOrBlank()
    }
}
