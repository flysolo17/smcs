package com.jmballangca.smcsmonitoringsystem.data.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    companion object {
        private const val PREF_NAME = "user_session"
        private const val KEY_UID = "uid"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    /**
     * Save UID after successful login
     */
    fun saveUid(uid: String) {
        editor.putString(KEY_UID, uid)
        editor.apply()
    }

    /**
     * Retrieve stored UID
     */
    fun getUid(): String? {
        return sharedPreferences.getString(KEY_UID, null)
    }

    /**
     * Clear stored UID (Logout)
     */
    fun clearUid() {
        editor.remove(KEY_UID)
        editor.apply()
    }
}