package com.example.themovie.ui.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import com.example.themovie.R
import com.example.themovie.util.IS_CHECKED
import com.example.themovie.util.NIGHT_MODE
import com.example.themovie.util.SharedPref

const val PAGE_MAIN = 0
const val PAGE_DETAILS = 1
const val PAGE_FAVORITE = 2

class MainActivity : AppCompatActivity() {

    private lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref = SharedPref(this)
        if (savedInstanceState == null) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            sharedPref.setSharedPref(IS_CHECKED, false)
            sharedPref.pagePreferences = 0
            supportFragmentManager.beginTransaction()
                .replace(R.id.main, MainFragment.newInstance())
                .commitNow()
        } else if (!sharedPref.getSharedPref(NIGHT_MODE))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        setContentView(R.layout.main_activity)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_navigation, menu)
        setMenuVisibility(sharedPref.pagePreferences)
        return true
    }

    override fun onBackPressed() {
        sharedPref.pagePreferences = 0
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.app_bar_settings -> {
                val list = arrayOf("Светлая тема", "Темная тема")
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Тема приложения").setItems(list) { _, which ->
                    when (which) {
                        0 -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                            sharedPref.setSharedPref(NIGHT_MODE, false)
                        }
                        1 -> {
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                            sharedPref.setSharedPref(NIGHT_MODE, true)
                        }
                    }
                    ActivityCompat.recreate(this)
                }
                builder.create().apply {
                    window?.setBackgroundDrawableResource(R.drawable.dialog_shape)
                    show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setMenuVisibility(page: Int) {
        findViewById<Toolbar>(R.id.toolbar).menu.apply {
            when (page) {
                PAGE_MAIN -> {
                    findItem(R.id.app_bar_notes).isVisible = false
                    findItem(R.id.average).isVisible = true
                    findItem(R.id.menu_favorite).isVisible = true
                }
                PAGE_FAVORITE -> {
                    findItem(R.id.app_bar_notes).isVisible = false
                    findItem(R.id.average).isVisible = false
                    findItem(R.id.menu_favorite).isVisible = false
                }
                PAGE_DETAILS -> {
                    findItem(R.id.app_bar_notes).isVisible = true
                    findItem(R.id.average).isVisible = false
                    findItem(R.id.menu_favorite).isVisible = false
                }
            }
        }
    }
}