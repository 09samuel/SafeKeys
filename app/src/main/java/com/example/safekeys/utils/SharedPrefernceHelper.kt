package com.example.safekeys.utils

import android.content.Context
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceHelper @Inject constructor(@ApplicationContext private val context: Context) {
    companion object{
        private const val MY_PREF_KEY ="MY_PREF"
    }

    fun isUserRegistered(): Boolean {
        val preferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        return preferences.getBoolean("is_registered", false)
    }

    fun setUserRegistered() {
        val preferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        preferences.edit().putBoolean("is_registered", true).apply()
    }

    fun isUserLoggedIn(): Boolean{
        val preferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        return preferences.getBoolean("is_logged_in", false)
    }

    fun setUserLogin() {
        val preferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        preferences.edit().putBoolean("is_logged_in", true).apply()
    }

    fun setUserLogOut() {
        val preferences = context.getSharedPreferences(MY_PREF_KEY, Context.MODE_PRIVATE)
        preferences.edit().putBoolean("is_logged_in", false).apply()
    }
}