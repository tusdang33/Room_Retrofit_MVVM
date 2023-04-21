package com.kaizm.learnmvvmandbinding.common

import android.content.SharedPreferences
import com.kaizm.learnmvvmandbinding.common.Const.PREFS_USER
import javax.inject.Inject

class SaveToken @Inject constructor(private val prefs: SharedPreferences) {
    fun saveToken(token: String) {
        prefs.edit().apply {
            putString(PREFS_USER, token)
            apply()
        }
    }

    fun getToken(): String? = prefs.getString(PREFS_USER, null)
}