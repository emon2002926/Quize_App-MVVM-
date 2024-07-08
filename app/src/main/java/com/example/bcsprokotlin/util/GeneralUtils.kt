package com.example.bcsprokotlin.util

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import java.text.NumberFormat
import java.util.Locale


object GeneralUtils {

    private val preferences: SharedPreferences? = null

    fun convertEnglishToBengaliNumber(numberStr: String): String {
        try {
            val number = numberStr.toDouble()
            val bengaliLocale = Locale("bn", "BD")
            val bengaliNumberFormat = NumberFormat.getNumberInstance(bengaliLocale)
            return bengaliNumberFormat.format(number)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            return numberStr
        }
    }

    fun convertBase64ToBitmap(base64Image: String?): Bitmap {
        val decodedString = Base64.decode(base64Image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    }


    fun hideShimmerLayout(shimmerLayout: ShimmerFrameLayout, recyclerView: RecyclerView) {
        shimmerLayout.stopShimmer()
        shimmerLayout.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    fun showShimmerLayout(shimmerLayout: ShimmerFrameLayout, recyclerView: RecyclerView) {
        shimmerLayout.startShimmer()
        shimmerLayout.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }


    fun EditText.isEmpty(errorMessage: String): Boolean {
        return if (this.text.toString().isEmpty()) {
            this.error = errorMessage
            true
        } else {
            false
        }
    }

//    var lastPosition = -1
//
//    fun setAnimation(ctx: Context?, viewToAnimate: View, position: Int) {
//        if (position > lastPosition) {
//            val slideIn = AnimationUtils.loadAnimation(ctx, R.anim.slide_in_left)
//            viewToAnimate.animation = slideIn
//            lastPosition = position
//        }
//    }


    private const val PREFS_NAME = "results_for"
    private var sharedPreferences: SharedPreferences? = null

    // Constructor
    fun DataManager(context: Context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Method to save an integer value in SharedPreferences
//    fun saveInt(key: String?, value: Int) {
//        val editor = sharedPreferences!!.edit()
//        editor.putInt(key, value)
//        editor.apply() // or editor.commit(); if you want synchronous saving
//    }

    // Method to get an integer value from SharedPreferences
//    fun getInt(key: String?, defaultValue: Int): Int {
//        return sharedPreferences!!.getInt(key, defaultValue)
//    }

    fun logger(message: String) = Log.d("lodkvgcvg", message)

}