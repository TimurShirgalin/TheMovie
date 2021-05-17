package com.example.themovie.util

import android.content.Context
import android.content.SharedPreferences

const val NIGHT_MODE = "NIGHT_MODE"

class SharedPref(context: Context) {
    private val SHARED_PREF = "SHARED_PREF"
    private var sP: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)

    fun setSharedPref(id: String, value: Boolean) = sP.edit().putBoolean(id, value).apply()


    fun getSharedPref(id: String): Boolean = sP.getBoolean(id, false)
}