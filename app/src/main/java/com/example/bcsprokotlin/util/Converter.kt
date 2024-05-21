package com.example.bcsprokotlin.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.text.NumberFormat
import java.util.Locale

object Converter {

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

}