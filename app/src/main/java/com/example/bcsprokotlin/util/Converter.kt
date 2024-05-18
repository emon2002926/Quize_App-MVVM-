package com.example.bcsprokotlin.util

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

}