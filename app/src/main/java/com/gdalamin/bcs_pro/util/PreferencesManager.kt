package com.gdalamin.bcs_pro.util

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(val context: Context) {

    private val PREFS_NAME = "results_for"
    private var sharedPreferences: SharedPreferences? = null

    fun DataManager(context: Context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    //     Method to save an integer value in SharedPreferences
    fun saveInt(key: String?, value: Int) {
        val editor = GeneralUtils.sharedPreferences!!.edit()
        editor.putInt(key, value)
        editor.apply() // or editor.commit(); if you want synchronous saving
    }

    //     Method to get an integer value from SharedPreferences
    fun getInt(key: String?, defaultValue: Int): Int {
        return GeneralUtils.sharedPreferences!!.getInt(key, defaultValue)
    }


}