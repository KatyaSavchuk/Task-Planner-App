package com.example.myapplication2.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ThemeViewModel(context: Context) : ViewModel() {

    private val sharedPreferences = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    private val _isDarkTheme = MutableStateFlow(getSavedTheme())
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme

    private fun getSavedTheme(): Boolean {
        val isDark = sharedPreferences.getBoolean("is_dark_theme", false)
        Log.d("ThemeViewModel", "Loaded theme: $isDark")
        return isDark
    }

    private fun saveTheme(isDark: Boolean) {
        sharedPreferences.edit().putBoolean("is_dark_theme", isDark).apply()
        Log.d("ThemeViewModel", "Saved theme: $isDark")
    }

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
        saveTheme(_isDarkTheme.value)
    }
}
