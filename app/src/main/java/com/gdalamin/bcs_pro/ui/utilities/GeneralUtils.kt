package com.gdalamin.bcs_pro.ui.utilities

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


    fun convertEnglishToBangla(numberStr: String): String {
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


    fun logger(message: String) = Log.d("lodkvgcvg", message)

}