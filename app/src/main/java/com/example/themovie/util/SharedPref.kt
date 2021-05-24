package com.example.themovie.util

import android.content.Context
import android.content.SharedPreferences

const val NIGHT_MODE = "NIGHT_MODE"
const val NOTE_ICON = "NOTE_ICON"
const val IS_CHECKED = "IS_CHECKED"
const val IS_MAIN = "IS_MAIN"
const val PAGE = "PAGE"

class SharedPref(context: Context) {
    private val SHARED_PREF = "SHARED_PREF"
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE)

    fun setSharedPref(id: String, value: Boolean) =
        sharedPreferences.edit().putBoolean(id, value).apply()

    var pagePreferences: Int
        get() = sharedPreferences.getInt(PAGE, 0)
        set(value) = sharedPreferences.edit().putInt(PAGE, value).apply()

    var noteIconPreferences: Boolean
        get() = sharedPreferences.getBoolean(NOTE_ICON, false)
        set(value) = sharedPreferences.edit().putBoolean(NOTE_ICON, value).apply()


    fun getSharedPref(id: String): Boolean = sharedPreferences.getBoolean(id, false)
}